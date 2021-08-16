package com.andymur.ot.ecospark

import org.apache.spark.ml.classification.DecisionTreeClassifier
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
 * Here we are creating our classification model which predicts iris type using input parameters.
 * We use iris.csv (available at kaggle: ) in order to train the model.
 */
object IrisClassifier extends App {

  // What to read:
  // https://medium.com/data-science-school/practical-apache-spark-in-10-minutes-part-4-mllib-fca02fecf5b8
  // https://towardsdatascience.com/your-first-apache-spark-ml-model-d2bb82b599dd

  // creating context
  val spark = SparkSession.builder()
    .appName("HW3")
    .config("spark.master", "local")
    .getOrCreate()

  val irisFacts = spark.read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("src/main/resources/data/iris.csv")

  val featureColumns = Array("sepal_length", "sepal_width", "petal_length", "petal_width")

  val vectorAssembler: VectorAssembler = new VectorAssembler()
    .setInputCols(featureColumns).setOutputCol("features")

  val irisFeatures = vectorAssembler.transform(irisFacts).drop("sepal_length")
    .drop("sepal_width")
    .drop("petal_length")
    .drop("petal_width")

  val indexer: StringIndexer = new StringIndexer().setInputCol("species").setOutputCol("labelIndex")

  val irisTransformed = indexer.fit(irisFeatures).transform(irisFeatures)

  val randomSplitData: Array[Dataset[Row]] = irisTransformed.randomSplit(Array(0.7, 0.3))

  val trainingData = randomSplitData(0)
  val testData = randomSplitData(1)

  val dt: DecisionTreeClassifier = new DecisionTreeClassifier().setLabelCol("labelIndex").setFeaturesCol("features")

  val model = dt.fit(trainingData)

  val predictions = model.transform(testData)

  predictions.show(60)

  import spark.implicits._
  val data = Seq((7.7,3.8,6.7,2.2))
  val myDF = data.toDF("sepal_length", "sepal_width", "petal_length", "petal_width")
  myDF.show()

  val myFeatures = vectorAssembler.transform(myDF).drop("sepal_length")
    .drop("sepal_width")
    .drop("petal_length")
    .drop("petal_width")

  myFeatures.show()
  val myPredictions = model.transform(myFeatures)

  // TODO: calculate the error
}
