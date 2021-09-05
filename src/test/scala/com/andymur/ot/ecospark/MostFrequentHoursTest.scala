package com.andymur.ot.ecospark

import com.andymur.ot.ecospark.DataApiRDDMostFrequentHours.findMostBusyHours
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.QueryTest.checkAnswer
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.scalatest.flatspec.AnyFlatSpec

class MostFrequentHoursTest extends AnyFlatSpec {

  implicit val spark = SparkSession.builder()
    .config("spark.master", "local")
    .appName("Test №1 for Big Data Application")
    .getOrCreate()

  // TODO: add more tests, e.g. with broken input (non happy path scenarios)
  it should "return table with hour & number of orders grouped and sorted by hour using test data set" in {
    val taxiFacts = Seq(
      Row("1", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "N", "1", "2", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      Row("2", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "Y", "1", "2", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      Row("3", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "N", "2", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      Row("4", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "Y", "2", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      Row("5", "2018-01-24 15:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "N", "3", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      Row("6", "2018-01-24 16:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "Y", "3", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      Row("7", "2018-01-24 17:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "N", "1", "2", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      Row("8", "2018-01-24 17:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "Y", "3", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      Row("9", "2018-01-24 17:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "N", "1", "2", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      Row("1", "2018-01-24 17:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "Y", "1", "2", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      Row("2", "2018-01-24 18:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "N", "2", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8"),
      Row("3", "2018-01-24 18:02:56", "2018-01-24 15:10:58", "1", "2.02", "1", "Y", "2", "1", "2", "8.5", "0.5", "0.5", "0.0", "0.0", "0.3", "9.8")
    )

    import spark.implicits._

    val taxiFactsRDD: RDD[Row] = spark.sparkContext.parallelize(taxiFacts)

    val tableRDD: RDD[String] = findMostBusyHours(taxiFactsRDD)

    val actualDistribution: DataFrame = tableRDD.toDF()
    actualDistribution.show()
    checkAnswer(
      actualDistribution,
      Row("15 5") ::
      Row("17 4") ::
      Row("18 2") ::
      Row("16 1") :: Nil
    )
  }
}
