create database if not exists volcanodb;
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
