# OTUS Spark Ecosystem Course. Fourth Homework. Working with RDD/DataFrame/DataSet in Spark.

### Goal

Within the homework you'll be working with DataSource API V2 in order to gain some knowledge of creating Spark custom data connectors. Your goal is to finish current solution for partitioning reading support. 

### Assignment

1. Clone the repo from [https://github.com/Gorini4/spark_datasource_example](https://github.com/Gorini4/spark_datasource_example)
2. Change the file src/main/scala/org/example/datasource/postgres/PostgresDatasource.scala in order to make PostgresqlSpec.scala test using more than one partition while reading. 
   (sie of the partition must be set via option, like that: .option("partitionSize", "10")).

### Solution

### How to check (compile, build & run, etc)
