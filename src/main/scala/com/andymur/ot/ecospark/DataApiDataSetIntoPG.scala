package com.andymur.ot.ecospark

import org.apache.spark.sql.SparkSession

object DataApiDataSetIntoPG extends App {

  // https://databricks.com/spark/getting-started-with-apache-spark/datasets

  // creating context
  val spark = SparkSession.builder()
    .appName("HW3")
    .config("spark.master", "local")
    .getOrCreate()

  //TODO: looks like a magic
  import spark.implicits._

  // May I not enumerate all the columns?
  // What if I'd like to use different names as in parquet source file?
  // Looks like I need to use a kind of schema somehow
  case class TaxiFact(
    vendorId: Int,
    tpep_pickup_datetime: String,
    dropOffDateTime: String,
    passengerCount: Int,
    tripDistance: Double,
    rateCodeId: Int,
    storeAndFwdFlag: String,
    startLocationId: Int,
    endLocationId: Int,
    paymentType: Int,
    fareAmount: Double,
    extra: Double,
    mtaTax: Double,
    tipAmount: Double,
    tollsAmount: Double,
    improvementSurcharge: Double,
    totalAmount: Double
  )

  /*
  root
  |-- VendorID: integer (nullable = true)
  |-- tpep_pickup_datetime: timestamp (nullable = true)
  |-- tpep_dropoff_datetime: timestamp (nullable = true)
  |-- passenger_count: integer (nullable = true)
  |-- trip_distance: double (nullable = true)
  |-- RatecodeID: integer (nullable = true)
  |-- store_and_fwd_flag: string (nullable = true)
  |-- PULocationID: integer (nullable = true) // TLC Taxi Zone in which the taximeter was engaged.
  |-- DOLocationID: integer (nullable = true) // TLC Taxi Zone in which the taximeter was disengaged.
  |-- payment_type: integer (nullable = true)
  |-- fare_amount: double (nullable = true)
  |-- extra: double (nullable = true)
  |-- mta_tax: double (nullable = true)
  |-- tip_amount: double (nullable = true)
  |-- tolls_amount: double (nullable = true)
  |-- improvement_surcharge: double (nullable = true)
  |-- total_amount: double (nullable = true)
  // data set description can be found here: https://docs.microsoft.com/en-us/azure/open-datasets/dataset-taxi-yellow?tabs=azureml-opendatasets
  */

  // reading facts into data set
  val ds = spark.read.parquet("src/main/resources/data/yellow_taxi_jan_25_2018").as[TaxiFact]
  println(ds.limit(5))
}
