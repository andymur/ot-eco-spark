#!/usr/bin/python3.7
import pyspark

master_url = 'spark://localhost:7077'

conf = pyspark.SparkConf()
conf.setMaster(master_url)
sc = pyspark.SparkContext(conf=conf)

big_list = range(10000)
rdd = sc.parallelize(big_list, 2)
odds = rdd.filter(lambda x: x % 2 != 0)
v = odds.take(5)
print(v)
