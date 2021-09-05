package com.andymur.ot.ecospark

import com.andymur.ot.ecospark.DataApiDataSetIntoPG.spark
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions.desc

// more about joins in Spark can be found here: https://sparkbyexamples.com/spark/spark-sql-dataframe-join/
// http://ot-cluster:8890/#/notebook/2GAS31SRP

object DataApiHomeWorkTaxi {
  // creating context
  val spark: SparkSession = SparkSession.builder()
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
  def readFacts(factsDataPath: String): DataFrame = {
    spark.read.parquet(factsDataPath)
  }

  /*root
  |-- LocationID: integer (nullable = true)
  |-- Borough: string (nullable = true)
  |-- Zone: string (nullable = true)
  |-- service_zone: string (nullable = true)
  */
  // reading location data
  def readLocations(locationsDataPath: String): DataFrame = {
    spark.read
      .option("delimiter", value = ",")
      .schema("LocationID integer, Borough string, Zone string, service_zone string")
      .csv(locationsDataPath)
  }

  def mostUsedAsStartLocations(taxiFacts: DataFrame, locations: DataFrame): DataFrame = {
    // joining by start area (PULocationID)
    val joinedByStartLocationDF = taxiFacts.join(locations, taxiFacts("PULocationID") === locations("LocationID"), "inner")
    // sorting by most used first
    joinedByStartLocationDF.groupBy("Borough").count().orderBy(desc("count"))
  }

  def writeMostUsedAsStartLocations(sortedByStartAreaCountInMostPopularOrder: DataFrame, path: String): Unit = {
    //TODO: how to use my own partitioning?
    sortedByStartAreaCountInMostPopularOrder.write.parquet(path)
  }

  def main(args: Array[String]): Unit = {
    // printing the result
    val sortedByStartAreaCountInMostPopularOrder = mostUsedAsStartLocations(readFacts("src/main/resources/data/yellow_taxi_jan_25_2018"), readLocations("src/main/resources/data/taxi_zones.csv"))
    // printing the result
    sortedByStartAreaCountInMostPopularOrder.show()
    // storing it into parquet files
    writeMostUsedAsStartLocations(sortedByStartAreaCountInMostPopularOrder, "src/main/resources/data/most_popular_starting_areas_jan_25_2018")
  }

}