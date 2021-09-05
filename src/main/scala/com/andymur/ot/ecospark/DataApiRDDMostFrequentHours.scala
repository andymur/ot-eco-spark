package com.andymur.ot.ecospark

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Row, SparkSession}

object DataApiRDDMostFrequentHours {

  //TODO: extract somewhere context creation not to copy-paste...but for quick checking it might be better to keep as it is all in one file

  // creating context
  val spark = SparkSession.builder()
    .appName("HW3")
    .config("spark.master", "local")
    .getOrCreate()

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

  // reading facts data
  def readFacts(factsLocation: String): RDD[Row] = {
    spark.read.parquet("src/main/resources/data/yellow_taxi_jan_25_2018").rdd
  }

  def findMostBusyHours(taxiFactsRDD: RDD[Row]): RDD[String] = {
    // grouping to show the most frequent order time, it could be grouped like day hour -> number of entries in our fact table
    val mostFrequentOrderTimes: RDD[(Integer, Int)] = taxiFactsRDD
      // creating RDD with one column -- hour value of the order (0-23)
      .map(
        row => Integer.valueOf(row(1).toString.split(" ")(1).split(":")(0)))
      // grouping RDDs by hour value
      .groupBy(
        el => el)
      // making a map (list of entries where each entry has a first unique key value) with hour as a key and number of orders as a value
      .mapValues(
        l => l.size)
      // sorting the result by number of orders in descending order
      .sortBy(_._2, ascending = false)

    // creating a final table RDD
    mostFrequentOrderTimes.map(r => r._1 + " " + r._2)
  }

  def saveResultIntoLocation(tableRDD: RDD[String], resultLocation: String): Unit = {
    tableRDD.saveAsTextFile(resultLocation)
  }

  def main(args: Array[String]): Unit = {
    val tableRDD: RDD[String] = findMostBusyHours(readFacts("src/main/resources/data/yellow_taxi_jan_25_2018"))
    println(tableRDD.collect().mkString("\n"))
    saveResultIntoLocation(tableRDD, "src/main/resources/data/most_frequent_order_times")
  }
}
