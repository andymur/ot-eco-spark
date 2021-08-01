# OTUS Spark Ecosystem Course. Fourth Homework. Working with RDD/DataFrame/DataSet in Spark.

### Goal

Within the homework you'll be working with DataSource API V2 in order to gain some knowledge of creating Spark custom data connectors. Your goal is to finish current solution for partitioning reading support. 

### Assignment

1. Clone the repo from [https://github.com/Gorini4/spark_datasource_example](https://github.com/Gorini4/spark_datasource_example)
2. Change the file src/main/scala/org/example/datasource/postgres/PostgresDatasource.scala in order to make PostgresqlSpec.scala test using more than one partition while reading. 
   (size of the partition must be set via option, like that: .option("partitionSize", "10")).

### Solution

Not sure if the solution is correct :-) 
What I did:
1. Added partitionSize option in the test.
2. Use it in the method planInputPartitions of com.andymur.ot.ecospark.PostgresScan class.

I'm not sure if that correct cause it is said ```size of the partition``` and not ```number of partitions```

### How to check (compile, build & run, etc)

Just run PostgresqlSpec.scala as a test from your IDE.
