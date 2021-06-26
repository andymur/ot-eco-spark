# OTUS Spark Ecosystem Course. First Homework.

### Assignment

Load  geographical data from ```https://raw.githubusercontent.com/mledoze/countries/master/countries.json```.

Among african countries select 10 countries with top area and store the result into the output file provided by the use

Data should be structured as below:
[{"name": <official country name>, "capital": <capital name>, "area": <area in sq km>},..]

In case there are several capitals for a country choose the first one.

### Solution

Can be found in ```src/main/scala/com/andymur/ot/ecospark/FirstHomeWorkRunner.scala```

### How to check, compile, build & run

#### Checkout

Solution stored in the git repo. For checkout simply do this

```git clone -b first_home_work https://github.com/andymur/ot-eco-spark.git```

#### Compile & Run

We can compile & run our solution with a help from SBT. Just run following commands from the project's directory.

* ```sbt compile```
* ```sbt "run <output file name>" (e.g. sbt "run out.json")```

#### Build JAR file & Run

Thanks to sbt-assembly plugin included here project/assembly.sbt we can build our project as JAR file with a command
```sbt assembly```

After we can run it with

* ```cd ./target/scala-<version> (e.g. ./target/scala-2.13/)```
* ```java -jar ot-eco-spark-assembly-1.0.jar <output file name> (e.g. java -jar ot-eco-spark-assembly-1.0.jar out.json)```