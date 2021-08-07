scalaVersion := "2.13.3"

name := "ot-eco-spark"
organization := "com.andymur"
version := "1.0"

mainClass := Some("com.andymur.ot.ecospark.KafkaHomeWorkRunner")

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-csv" % "1.9.0",
  // adding Kafka
  "org.apache.kafka" % "kafka-clients" % "2.8.0"
)

