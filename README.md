# OTUS Spark Ecosystem Course. Third Homework. Working with RDD/DataFrame/DataSet in Spark.

### Goal

After completion of this homework you'll gain an experience working with RDD / DataFrame and DataSet APIs. You will be able to build DataMart based on raw data using Spark and various APIs. 

### Assignment

#### First part

Create the first data frame containing facts from the data which are available in parquet format (src/main/resources/data/yellow_taxi_jan_25_2018).
Create the second data frame containing locations from the data available in csv format (src/main/resources/data/taxi_zones.csv).
Using Spark DataFrame DSL create a table with most popular areas of taxi orders. Print the result and also store it into the output parquet file.

#### Second part

Create RDD containing facts from the data available in parquet format (src/main/resources/data/yellow_taxi_jan_25_2018).
Create lambda function to build a table showing the most frequent order time. Print the result and also store into the space separated text file.

#### Third part

Create DataSet containing facts from the data available in parquet format (src/main/resources/data/yellow_taxi_jan_25_2018).
Using Spark DataSet DSL and lambda function create the table showing ride distance distribution. 
Print the result and store it into the PostgreSQL DB table. For that purpose design and create the table structure and store it within the project.

i.e., you end up with Data Mart containing different columns (e.g., total number of rides, average ride distance, min and max ride distance etc).

### Solution

#### First part

Can be found in ```src/main/scala/com/andymur/ot/ecospark/DataApiHomeWorkTaxi.scala``` or as Zeppelin notebook ```src/main/resources/OT-ECO-HW3.1.json```

#### Second part

Can be found in ```src/main/scala/com/andymur/ot/ecospark/DataApiRDDMostFrequentHours.scala``` or as Zeppelin notebook ```src/main/resources/OT-ECO-HW3.2.json```

#### Third part

Make sure you started postgresql docker image. You can do it running ```docker-compose up``` from the project directory.
After you can run the application which can be found in ```src/main/scala/com/andymur/ot/ecospark/DataApiDataSetIntoPG.scala```

### How to check (compile, build & run, etc)

#### Checkout

Solution stored in the git repo. For checkout simply do this:

```git clone -b third_home_work_datamart https://github.com/andymur/ot-eco-spark.git``` or check the link ```https://github.com/andymur/ot-eco-spark/tree/third_home_work_datamart```

#### Compile & Run

We can compile & run our solution with a help from SBT or from IDE, see appropriate solution files above in "Solution" section (one file per each homework part).

#### Use with Yandex Cloud

You can also check Zeppelin notebook (one per each homework part). Import & run them (beware of different path you can have in your case, usually all paths are stored in the variables in the very beginning of the notebook).