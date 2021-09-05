package com.andymur.ot.ecospark

import com.andymur.ot.ecospark.DataApiHomeWorkTaxi.{mostUsedAsStartLocations, readFacts, readLocations}
import org.apache.spark.sql.QueryTest.checkAnswer
import org.apache.spark.sql.test.SharedSparkSession
import org.apache.spark.sql.{DataFrame, Row}

class MostUsedStartsTest extends SharedSparkSession {

  test ("find most used places as a start with test data set") {

    val taxiFactsSchema = Seq(
      "VendorID", "tpep_pickup_datetime", "tpep_dropoff_datetime", "passenger_count", "trip_distance", "RatecodeID",
      "store_and_fwd_flag", "PULocationID", "DOLocationID", "payment_type", "fare_amount", "extra",
      "mta_tax", "tip_amount", "tolls_amount", "improvement_surcharge", "total_amount"
    )

    // 5 records with ID 1 as a start location
    // 4 records with ID 2 as a start location
    // 3 records with ID 3 as a start location
    val taxiFacts = Seq(
      ("1", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "N", "1", "2", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      ("2", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "Y", "1", "2", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      ("3", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "N", "2", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      ("4", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "Y", "2", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      ("5", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "N", "3", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      ("6", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "Y", "3", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      ("7", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "N", "1", "2", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      ("8", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "Y", "3", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      ("9", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "N", "1", "2", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      ("1", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "Y", "1", "2", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      ("2", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "N", "2", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      ("3", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "Y", "2", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8")
    )

    val taxiFactsDF: DataFrame = spark.createDataFrame(taxiFacts).toDF(taxiFactsSchema:_*)
    val taxiLocationsSchema = Seq("LocationID", "Borough", "Zone", "service_zone")

    // no location for records with ID 3 (should be filtered after join)
    val taxiLocations = Seq(
      (null, "Nowhereland", "Zone51", "self-service"),
      ("1", "Queens", "Jamaica Bay", "BORO"),
      ("2", "Manhattan", "Alphabet", "yeLLow"),
    )


    val taxiLocationsDF: DataFrame =  spark.createDataFrame(taxiLocations).toDF(taxiLocationsSchema:_*)
    val actualDistribution: DataFrame = mostUsedAsStartLocations(taxiFactsDF, taxiLocationsDF)

    checkAnswer(
      actualDistribution,
      Row("Queens", 5) :: Row("Manhattan", 4) :: Nil
    )
  }

  test("find most used places as a start with real data") {
    val actualDistribution = mostUsedAsStartLocations(readFacts("src/main/resources/data/yellow_taxi_jan_25_2018"), readLocations("src/main/resources/data/taxi_zones.csv"))

    checkAnswer(
      actualDistribution,
        Row("Manhattan", 304266) ::
        Row("Queens", 17712) ::
        Row("Unknown", 6644) ::
        Row("Brooklyn", 3037) ::
        Row("Bronx", 211) ::
        Row("EWR", 19) ::
        Row("Staten Island", 4) ::  Nil
    )

  }
}
