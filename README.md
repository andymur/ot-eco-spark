# OTUS Spark Ecosystem Course. Second Homework. Working with HDFS using Scala.

### Assignment

Create an application which moves all the data from hdfs://stage to hdfs://ods using following rules:
structure of directories (date=<date>) must be preserved but all files in each directory must be merged into one

Example:
we have directory /stage/date=2020-11-11 with the following files part-0000.csv, part-0001.csv
afterwards there should be directory /ods/date=2020-11-11 with the one file part-0000.csv which has all the date from the two above mentioned files.

### Solution

Can be found in ```src/main/scala/com/andymur/ot/ecospark/HdfsHomeWorkRunner.scala```

### How to check, compile, build & run

#### Checkout

Solution stored in the git repo. For checkout simply do this

```git clone -b second_home_work_hdfs https://github.com/andymur/ot-eco-spark.git```

#### Compile & Run

We can compile & run our solution with a help from SBT. Just run following commands from the project's directory.

* ```sbt compile```
* ```sbt run```

Before running the application please make sure that all prerequisites are there, hadoop cluster is up and running and source data has been loaded

#### Build JAR file & Run

Thanks to sbt-assembly plugin included here project/assembly.sbt we can build our project as JAR file with a command
```sbt assembly```

After we can run it with

* ```cd ./target/scala-<version> (e.g. ./target/scala-2.13/)```
* ```java -jar ot-eco-spark-assembly-1.0.jar```