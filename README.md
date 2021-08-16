# OTUS Spark Ecosystem Course. Sixth Homework. Working with Spark Structured Streaming.

### Goal

After homework completion you'll be able to train and store Spark ML models. In the second part of this HW you'll be using Spark ML models along with Spark Structured Streaming.

### Assignment

#### First part

Build and train model for Iris classification. Data set description can be found here: https://ru.wikipedia.org/wiki/%D0%98%D1%80%D0%B8%D1%81%D1%8B_%D0%A4%D0%B8%D1%88%D0%B5%D1%80%D0%B0.  
Data set in csv format can be found here: https://www.kaggle.com/arshid/iris-flower-dataset
In the end you should provide either notebook or program.

#### Second part

Create an application which reads from the Apache Kafka topic (e.g. "input") csv records with 4 features and returns prediction result into another Apache Kafka topic (e.g. "prediction")

### Solution

#### First part

Can be found in ```src/main/scala/com/andymur/ot/ecospark/IrisClassifier.scala```

#### Second part

Can be found in ```src/main/scala/com/andymur/ot/ecospark/KafkaStreamReaderWriter.scala```

### How to check (compile, build & run, etc)

#### Checkout

Solution stored in the git repo. For checkout simply do this:

```git clone -b sixth_home_work_structured_streaming https://github.com/andymur/ot-eco-spark.git``` or check the link ```https://github.com/andymur/ot-eco-spark/tree/sixth_home_work_structured_streaming```

#### Compile & Run

We can compile & run our solution with a help from SBT or from IDE, see appropriate solution files above in "Solution" section (one file per each homework part).