package com.andymur.ot.ecospark

import com.andymur.ot.ecospark.DataApiHomeWorkTaxi.{mostUsedAsStartLocations, readFacts, readLocations}
import org.apache.spark.sql.QueryTest.checkAnswer
import org.apache.spark.sql.Row
import org.apache.spark.sql.test.SharedSparkSession

class MostUsedStartsTest extends SharedSparkSession {

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
