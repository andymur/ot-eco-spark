package com.andymur.ot.ecospark

import org.apache.spark.sql.SparkSession

import java.sql.{Connection, DriverManager}

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

  // write to postgres
  val DB_NAME = "docker"
  val DB_USER = "docker"
  val DB_PASS = "docker"
  // what about formatters?
  val con_str = "jdbc:postgresql://localhost:5432/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASS
  val conn = DriverManager.getConnection(con_str)
  try {
    //get facts as data set of TaxiFact objects
    val result = df.as[TaxiFact]
      .collect()
    // group them by hour
      .groupBy(fact => hourFromTime(fact.pickUpDateTime))
    // make a record from the grouped TaxiFact objects
      .mapValues(a => RecFromFacts(a))
    println(result)
    // write each group in the postgresql as a table's row
    result.foreach(h => write(conn)(h._1, h._2))
  } finally {
    if (conn != null) {
      conn.close()
    }
  }

  // function to map taxi fact values into record of aggregates for database single record
  def RecFromFacts(facts: Array[TaxiFact]): DataMartRec = {
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
    DataMartRec(numberOfTrips, minDistance, maxDistance, totalDistance / numberOfTrips, minPaycheck, maxPaycheck, totalPaycheck / numberOfTrips)
  }

  // parses the time string and returns hour values
  def hourFromTime(time: String): Int = {
    Integer.valueOf(time.split(" ")(1).split(":")(0))
  }

  // inserts one row
  def write(conn: Connection)(hour: Int, rec: DataMartRec): Unit = {
    val query = "INSERT INTO pickup_by_hour " +
      " VALUES (?, ?, ?, ?, ?, ?, ?, ?)"

    val stm = conn.prepareStatement(query)
    stm.setInt(1, hour)
    stm.setInt(2, rec.totalRides)
    stm.setDouble(3, rec.minDistance)
    stm.setDouble(4, rec.maxDistance)
    stm.setDouble(5, rec.averageDistance)
    stm.setDouble(6, rec.minPaycheck)
    stm.setDouble(7, rec.maxPaycheck)
    stm.setDouble(8, rec.averagePaycheck)
    stm.executeUpdate()
  }
}
