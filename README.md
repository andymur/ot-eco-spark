# OTUS Spark Ecosystem Course. Eighth Homework. Hive.

### Goal

After completion of this homework you will be able to work with Hive (i.e. setup hive cluster and run queries against it).

### Assignment

Create 5-6 data marts (queries) using Hive (be sure to use ```where, count, group by, having, order by, join, union, window``` operations). Data set can be taken from Kaggle.

### Solution

I used https://hshirodkar.medium.com/apache-hive-on-docker-4d7280ac6f8e as general reference guide.

Also what was used during homework:

* https://bigdataprogrammers.com/windowing-functions-in-hive/
* https://cwiki.apache.org/confluence/display/hive/languagemanual+windowingandanalytics


Decided to take data set about volcanos https://www.kaggle.com/jessemostipak/volcano-eruptions?select=volcano.csv

### How to check (compile, build & run, etc)

Run docker compose ```docker-compose up```

After you have to go through some steps:

Go inside hive container ```docker exec -it hive-server /bin/bash```
CD to the dataset directory ```cd ../dataset/```

Create database & table structure ```hive -f volcano.hql```

I leave volcano.hql content here:

```create database if not exists volcanodb;
use volcanodb;

create external table if not exists volcano (
  volcanoid int,
  volcanoname string,
  volcanotype string,
  lasteruption int,
  country string,
  region string,
  subregion string
)
row format delimited
fields terminated by ','
lines terminated by '\n'
stored as textfile location 'hdfs://namenode:8020/user/hive/warehouse/volcanodb/volcano'
TBLPROPERTIES ("skip.header.line.count"="1");


create external table if not exists eruption (
  volcanoid int,
  volcanoname string,
  eruptionid int,
  eruptioncategory string,
  areaofactivity string,
  vei string,
  startyear int,
  startmonth int,
  startday int,
  evidencemethod string,
  endyear int,
  endmonth int,
  endday   int,
  latitude double,
  longitude double
)
row format delimited
fields terminated by ','
lines terminated by '\n'
stored as textfile location 'hdfs://namenode:8020/user/hive/warehouse/volcanodb/eruption'
TBLPROPERTIES ("skip.header.line.count"="1");

create external table if not exists event (
  volcanoid int,
  volcanoname string,
  eruptionid int,
  eruptionstartyear int,
  eventid int,
  eventtype string,
  eventremarks string,
  eventyear int,
  eventmonth int,
  eventday int  
)
row format delimited
fields terminated by ','
lines terminated by '\n'
stored as textfile location 'hdfs://namenode:8020/user/hive/warehouse/volcanodb/event'
TBLPROPERTIES ("skip.header.line.count"="1");
```

Put the data files to HDFS:

```
hadoop fs -put volcano.csv hdfs://namenode:8020/user/hive/warehouse/volcanodb/volcano
hadoop fs -put eruptions.csv hdfs://namenode:8020/user/hive/warehouse/volcanodb/eruption/
hadoop fs -put events.csv hdfs://namenode:8020/user/hive/warehouse/volcanodb/event/
```

After that run following commands from inside the hive container:

```hive```

Select database and do a simple check:

```
show databases;
use volcanodb;
select * from volcanodb.volcano limit 10;
```

#### Checkout

Solution stored in the git repo. For checkout simply do this:

```git clone -b eighth_home_work_hive https://github.com/andymur/ot-eco-spark.git``` or check the link ```https://github.com/andymur/ot-eco-spark/tree/eighth_home_work_hive```

#### Datamarts

1. Countries with volcanos errupted (in total) more than thousand times:

```select country, count(*) from volcano join eruption on volcano.volcanoid = eruption.volcanoid group by country having count(*) > 1000 order by country;```

Result:
```
Chile	1961
Ecuador	1107
Iceland	2169
Indonesia	6218
Italy	2612
Japan	7335
New Zealand	1637
Nicaragua	1040
Papua New Guinea	1158
Russia	3911
United States	4561
Time taken: 11.35 seconds, Fetched: 11 row(s)
```

2. Countries with volcanos with confirmed erruptions (in total) more than thousand times.

```select country, count(*) from volcano join eruption on volcano.volcanoid = eruption.volcanoid where eruptioncategory = "Confirmed Eruption" group by country having count(*) > 1000 order by country;```

Result (as you can see here that list is much smaller):

```
Indonesia	1177
Japan	1423
Time taken: 11.391 seconds, Fetched: 2 row(s)
```

3. Top ten volcanos (from top to bottom) with most recorded events (each eruption contains several events).

```select count(event.eventid) AS cnt, event.volcanoname from event join eruption on eruption.eruptionid = event.eruptionid group by event.volcanoname order by  cnt desc limit 10;```

Result:

```
11058	Merapi
9976	Etna
5808	Kizimen
5550	Vesuvius
5310	Stromboli
4460	Mayon
4360	Asosan
3934	Kilauea
3362	Bezymianny
3292	Klyuchevskoy
Time taken: 11.513 seconds, Fetched: 10 row(s)```


4. Top ten russian volcanos (from top to bottom) with most recorded events (each eruption contains several events).

```select count(event.eventid) AS cnt, event.volcanoname from event join eruption on eruption.eruptionid = event.eruptionid join volcano on volcano.volcanoid = eruption.volcanoid where country = "Russia" group by event.volcanoname order by  cnt desc limit 10;```

Result:
```
3362	Bezymianny
3292	Klyuchevskoy
3272	Sheveluch
1410	Avachinsky
1400	Tolbachik
1148	Karymsky
1030	Gorely
582	Mutnovsky
544	Alaid
488	Ksudach
```


5. One (first) volcano per country.

```select distinct country, FIRST_VALUE(volcanoname) OVER (partition by country order by country) from volcano;```

Result:
```
Algeria	Manzaz Volcanic Field
Antarctica	Andrus
Argentina	Domuyo
Armenia	Vaiyots-Sar
Armenia-Azerbaijan	Porak
Australia	Newer Volcanics Province
Bolivia	Jatun Mundo Quri Warani
Burma (Myanmar)	Popa
Cameroon	Cameroon
Canada	Spectrum Range
Cape Verde	Fogo
Chile	Easter Island
Chile-Argentina	San Jose
Chile-Bolivia	Sairecabur
Chile-Peru	Tacora
China	Hainan Volcanic Field
...
```