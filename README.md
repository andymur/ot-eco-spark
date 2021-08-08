# OTUS Spark Ecosystem Course. Fifth Homework. Working with Apache Kafka.

### Goal

To learn work with Apache Kafka. Handle Apache Kafka topics. Create producers and consumers for Apache Kafka in Scala programming language.

### Prerequisites

Before starting the assignment please start Apache Kafka cluster up (you can use docker compose: https://github.com/Gorini4/kafka_scala_example) and create topic ```books``` with three partitions.

### Assignment

Create an application which reads the data from csv data file (https://www.kaggle.com/sootersaalu/amazon-top-50-bestselling-books-2009-2019) and loads them into above mentioned topic in JSON format.
After that, it should read the last 5 records of each partition from the topic (last means with the greatest offset value). While reading from the topic, you're allowed to keep not more than 15 records at a time.

### Solution

Can be found here ```com.andymur.ot.ecospark.KafkaHomeWorkRunner```

### How to check, compile, build & run

#### Checkout

Solution stored in the git repo. For checkout simply do this

```git clone -b fifth_home_work_kafka https://github.com/andymur/ot-eco-spark.git```

Or go and check on github: [link](https://github.com/andymur/ot-eco-spark/tree/fifth_home_work_kafka)

#### Compile & Run

We can compile & run our solution with a help from SBT. Just run following commands from the project's directory.

* ```sbt compile```
* ```sbt run```

You can also run ```com.andymur.ot.ecospark.KafkaHomeWorkRunner``` from IDE.

Just not forget to start up the Apache Kafka broker with docker-compose and create appropriate topic with command below:
```./kafka-topics.sh --bootstrap-server localhost:29092 --create --topic books --if-not-exists --partitions 3```
