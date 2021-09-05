# OTUS Spark Ecosystem Course. Seventh Homework. Testing Spark applications.

### Goal

After completion of this homework you will be able to write tests for Spark applications. 

### Assignment

Refactor applications created for 3rd homework (Datamart). Cover them with unit tests.

### Solution

I refactored ```com.andymur.ot.ecospark.DataApiRDDMostFrequentHours``` application which reads NY taxi dataset and creates table with hour and number of orders as columns sorted by the number of orders.
```com.andymur.ot.ecospark.MostFrequentHoursTest``` test created to check its work.

Another application refactored ```com.andymur.ot.ecospark.DataApiHomeWorkTaxi```. It creates a DataFrame with district names and number of rides started in the appropriate district.
With a help of such refactoring we can test it more easily. For that purpose  we have ```com.andymur.ot.ecospark.MostUsedStartsTest``` test.

### How to check (compile, build & run, etc)

#### Checkout

Solution stored in the git repo. For checkout simply do this:

```git clone -b seventh_home_work_spark_testing https://github.com/andymur/ot-eco-spark.git``` or check the link ```https://github.com/andymur/ot-eco-spark/tree/seventh_home_work_spark_testing```

#### Compile & Run

We can compile & run our solution with a help from SBT or from IDE. Tests you can run from appropriate test classes ```com.andymur.ot.ecospark.MostFrequentHoursTest``` & ```com.andymur.ot.ecospark.MostFrequentHoursTest```.
Or ```sbt test```