package com.andymur.ot.ecospark

import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}

object DataApiDataSetIntoPG extends App {

  // https://databricks.com/spark/getting-started-with-apache-spark/datasets

  // creating context
  val spark = SparkSession.builder()
    .appName("HW3")
    .config("spark.master", "local")
    .getOrCreate()

  //TODO: looks like a magic
  import spark.implicits._


  case class TaxiFact(
    // convert me to date time
    pickUpDateTime: String,
    dropOffDateTime: String,
    totalAmount: Double,
    tripDistance: Double
  )

  // case class represents db record values
  case class DataMartRec(
    hourOfDay: Int,
    totalRides: Int,
    minDistance: Double,
    maxDistance: Double,
    averageDistance: Double,
    minPaycheck: Double,
    maxPaycheck: Double,
    averagePaycheck: Double
  )

  val factsLocation = "src/main/resources/data/yellow_taxi_jan_25_2018"

  // reading facts into the data frame
  val df = spark.read.parquet(factsLocation)
    .withColumnRenamed("tpep_pickup_datetime", "pickUpDateTime")
    .withColumnRenamed("tpep_dropoff_datetime", "dropOffDateTime")
    .withColumnRenamed("total_amount", "totalAmount")
    .withColumnRenamed("trip_distance", "tripDistance")
    .drop(
      "VendorID", "passenger_count", "RatecodeID", "store_and_fwd_flag", "PULocationID",
      "DOLocationID", "payment_type", "fare_amount", "extra", "mta_tax", "tip_amount", "tolls_amount", "improvement_surcharge"
    )

  // write to postgres part

  // set all options
  val DB_DRIVER = "org.postgresql.Driver"
  val DB_URL = "jdbc:postgresql://localhost:5432/"
  val DB_TABLE = "pickup_by_hour"
  val DB_NAME = "docker"
  val DB_USER = "docker"
  val DB_PASS = "docker"

  //get facts as data set of TaxiFact objects and convert them to the data set of records RecFromFacts
  val result: Dataset[DataMartRec] = df.as[TaxiFact]
    .collect()
  // group them by hour
    .groupBy(fact => hourFromTime(fact.pickUpDateTime))
  // make a data set of records from the grouped TaxiFact objects
    .map(entry => RecFromFacts(entry._1, entry._2)).toSeq.toDS()

  // print the result (our DataMart)
  result.sort("hourOfDay").show(24)
  // store it to the postgresql table
  result
    .withColumnRenamed("hourOfDay", "hour_of_day")
    .withColumnRenamed("totalRides", "total_trips")
    .withColumnRenamed("minDistance", "min_distance")
    .withColumnRenamed("maxDistance", "max_distance")
    .withColumnRenamed("averageDistance", "avg_distance")
    .withColumnRenamed("minPaycheck", "min_paycheck")
    .withColumnRenamed("maxPaycheck", "max_paycheck")
    .withColumnRenamed("averagePaycheck", "avg_paycheck")
    .write
    .format("jdbc")
    .mode(SaveMode.Overwrite)
    .option("driver", DB_DRIVER)
    .option("url", DB_URL)
    .option("dbtable", DB_TABLE)
    .option("user", DB_USER)
    .option("password", DB_PASS)
    .save()

  // function to map taxi fact values into record of aggregates for database single record
  def RecFromFacts(hourOfDay: Int, facts: Array[TaxiFact]): DataMartRec = {
    var minDistance = Double.MaxValue
    var maxDistance = 0.0
    var totalDistance: Double = 0.0
    var numberOfTrips: Integer = 0
    var minPaycheck = Double.MaxValue
    var maxPaycheck = 0.0
    var totalPaycheck = 0.0

    // I don't want to consider short trips
    facts.filter(f => f.tripDistance >= 0.5)
      .foreach(f => {
        minDistance = Math.min(minDistance, f.tripDistance)
        maxDistance = Math.max(maxDistance, f.tripDistance)
        totalDistance += f.tripDistance
        minPaycheck = Math.min(minPaycheck, f.totalAmount)
        maxPaycheck = Math.max(maxPaycheck, f.totalAmount)
        totalPaycheck += f.totalAmount
        numberOfTrips += 1
    })
    DataMartRec(hourOfDay, numberOfTrips, minDistance, maxDistance, totalDistance / numberOfTrips, minPaycheck, maxPaycheck, totalPaycheck / numberOfTrips)
  }

  // parses the time string and returns hour values
  def hourFromTime(time: String): Int = {
    Integer.valueOf(time.split(" ")(1).split(":")(0))
  }
}
