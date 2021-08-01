organization := "com.andymur"
name := "ot-eco-spark"
version := "1.0"
scalaVersion := "2.12.13"

val testcontainersScalaVersion = "0.38.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "3.0.1",
  "org.postgresql" % "postgresql" % "42.2.18",

  "org.scalatest" %% "scalatest" % "3.2.2" % "test",
  "com.dimafeng" %% "testcontainers-scala-scalatest" % testcontainersScalaVersion % "test",
  "com.dimafeng" %% "testcontainers-scala-postgresql" % testcontainersScalaVersion % "test",
)

Test / fork := true
