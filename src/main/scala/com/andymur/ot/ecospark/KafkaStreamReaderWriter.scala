package com.andymur.ot.ecospark

import org.apache.spark.ml.classification.{DecisionTreeClassificationModel, DecisionTreeClassifier}
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
 * This program reads csv rows from the kafka topic with iris feature values.
 * It does it in a streaming fashion.
 * It applies regression model in order to classify the iris and writes the result (row with features and predicted value) into another topic
 */
object KafkaStreamReaderWriter extends App {

  // Some useful kafka commands:
  // .//kafka-topics.sh --bootstrap-server localhost:29092 --topic <topic>
  // ./kafka-topics.sh --bootstrap-server localhost:29092 --create --topic <topic>
  // ./kafka-console-consumer.sh --bootstrap-server localhost:29092 --from-beginning --topic <topic> --timeout-ms 2000
  // ./kafka-console-producer.sh --bootstrap-server localhost:29092 --topic <topic>

  // What to read: https://sparkbyexamples.com/spark/spark-streaming-with-kafka/

  val in_topic_name = "input"
  val out_topic_name = "prediction"

  // creating context
  val spark = SparkSession.builder()
    .appName("HW3")
    .config("spark.master", "local")
    .getOrCreate()

  val featureColumns = Array("sepal_length", "sepal_width", "petal_length", "petal_width")

  val vectorAssembler: VectorAssembler = new VectorAssembler()
    .setInputCols(featureColumns).setOutputCol("features")

  val model = trainModel(spark, vectorAssembler)

  val df: DataFrame = spark.readStream
    .format("kafka")
    .option("kafka.bootstrap.servers", "localhost:29092")
    .option("subscribe", in_topic_name)
    //.option("startingOffsets", "earliest")
    .load()

  val valueDF: DataFrame = df.selectExpr("CAST(value AS STRING)")

  import spark.implicits._

  // I have no idea how to make it nicer...it looks pretty ugly :-(

  // we have a row from kafka topic
  val mappedDF = valueDF.map(row => {
    val rawValue = row.getString(0)
    // we split it into 4 double feature values
    val features = rawValue.split(";").map(el => el.toDouble)

    // we make DF out of it
    val rowDF = Seq((features(0), features(1), features(2), features(3))).toDF("sepal_length", "sepal_width", "petal_length", "petal_width")

    // we create DF with feature column for predictor
    val myFeatures = vectorAssembler.transform(rowDF).drop("sepal_length")
      .drop("sepal_width")
      .drop("petal_length")
      .drop("petal_width")

    // we create DF with prediction value
    val prediction = model.transform(myFeatures)

    // creating string with feature values + prediction (class)
    (features :+ prediction.first().getDouble(3)).mkString(";")
  })

  // out to the console
  mappedDF
    .writeStream
    .format("console")
    .outputMode("append")
    .start()
    .awaitTermination()

  // and also to the kafka topic

  mappedDF.writeStream
    .format("kafka")
    .outputMode("append")
    .option("kafka.bootstrap.servers", "localhost:9092")
    .option("topic", out_topic_name)
    .start()
    .awaitTermination()


  // here we train our model in the very beginning, similar what we have in IrisClassifier
  def trainModel(spark: SparkSession, vectorAssembler: VectorAssembler): DecisionTreeClassificationModel = {
    val irisFacts = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("src/main/resources/data/iris.csv")

    val irisFeatures = vectorAssembler.transform(irisFacts).drop("sepal_length")
      .drop("sepal_width")
      .drop("petal_length")
      .drop("petal_width")

    val indexer: StringIndexer = new StringIndexer().setInputCol("species").setOutputCol("labelIndex")

    val irisTransformed = indexer.fit(irisFeatures).transform(irisFeatures)

    val randomSplitData: Array[Dataset[Row]] = irisTransformed.randomSplit(Array(0.7, 0.3))

    val trainingData = randomSplitData(0)

    val dt: DecisionTreeClassifier = new DecisionTreeClassifier().setLabelCol("labelIndex").setFeaturesCol("features")

    dt.fit(trainingData)
  }
}
