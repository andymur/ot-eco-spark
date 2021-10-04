# OTUS Spark Ecosystem Course. Seventh Homework. Testing Spark applications.

### Goal

After completion of this homework you will be able to work with Hive.

### Assignment

Create 5-6 data marts (queries) using Hive (be sure to use ```where, count, group by, having, order by, join, union, window``` operations). Data set can be taken from Kaggle.

### Solution

Decided to take data set about volcanos https://www.kaggle.com/jessemostipak/volcano-eruptions?select=volcano.csv

### How to check (compile, build & run, etc)

Run docker compose 

#### Checkout

Solution stored in the git repo. For checkout simply do this:

```git clone -b seventh_home_work_spark_testing https://github.com/andymur/ot-eco-spark.git``` or check the link ```https://github.com/andymur/ot-eco-spark/tree/seventh_home_work_spark_testing```

#### Compile & Run

We can compile & run our solution with a help from SBT or from IDE. Tests you can run from appropriate test classes ```com.andymur.ot.ecospark.MostFrequentHoursTest``` & ```com.andymur.ot.ecospark.MostFrequentHoursTest```.
Or ```sbt test```