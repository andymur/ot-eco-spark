scalaVersion := "2.13.3"
val circeVersion = "0.14.1"

name := "ot-eco-spark"
organization := "com.andymur"
version := "1.0"

mainClass := Some("com.andymur.ot.ecospark.KafkaHomeWorkRunner")

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2"

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-csv" % "1.9.0",
  // adding Kafka
  "org.apache.kafka" % "kafka-clients" % "2.8.0"
)

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)