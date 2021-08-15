package com.andymur.ot.ecospark

import com.dimafeng.testcontainers.scalatest.TestContainerForAll
import com.dimafeng.testcontainers.PostgreSQLContainer
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.scalatest.flatspec.AnyFlatSpec

import java.sql.DriverManager
import java.util.Properties

class PostgresqlSpec extends AnyFlatSpec with TestContainerForAll {

  override val containerDef : PostgreSQLContainer.Def = PostgreSQLContainer.Def()

  val testTableName = "users"

  "PostgreSQL data source" should "read table" in withContainers { postgresServer =>
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("PostgresReaderJob")
      .getOrCreate()

    spark
      .read
      .format("com.andymur.ot.ecospark")
      .option("url", postgresServer.jdbcUrl)
      .option("user", postgresServer.username)
      .option("password", postgresServer.password)
      .option("tableName", testTableName)
      .option("partitionNum", 10)
      .load()
      .show()

    spark.stop()
  }

  "PostgreSQL data source" should "write table" in withContainers { postgresServer =>
    val spark = SparkSession
      .builder()
      .master("local[*]")
      .appName("PostgresWriterJob")
      .getOrCreate()

    import spark.implicits._

    val df = (60 to 70).map(_.toLong).toDF("user_id")

    df
      .write
      .format("com.andymur.ot.ecospark")
      .option("url", postgresServer.jdbcUrl)
      .option("user", postgresServer.username)
      .option("password", postgresServer.password)
      .option("tableName", testTableName)
      .mode(SaveMode.Append)
      .save()

    spark.stop()
  }

  override def afterContainersStart(container: Containers): Unit = {
    super.afterContainersStart(container)

    container match {
      case c: PostgreSQLContainer =>
        val conn = connection(c)
        val stmt1 = conn.prepareStatement(Queries.createTableQuery)
        stmt1.execute
        val stmt2 = conn.prepareStatement(Queries.insertDataQuery)
        stmt2.execute
        conn.close()
    }
  }

  def connection(c: PostgreSQLContainer) = {
    Class.forName(c.driverClassName)
    val properties = new Properties()
    properties.put("user", c.username)
    properties.put("password", c.password)
    DriverManager.getConnection(c.jdbcUrl, properties)
  }

  object Queries {
    lazy val createTableQuery = s"CREATE TABLE $testTableName (user_id BIGINT PRIMARY KEY);"

    lazy val testValues: String = (1 to 50).map(i => s"($i)").mkString(", ")

    lazy val insertDataQuery = s"INSERT INTO $testTableName VALUES $testValues;"
  }

}
