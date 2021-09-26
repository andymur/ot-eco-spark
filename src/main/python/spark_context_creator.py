#!/usr/bin/python3.7

def spark_context_creator():
    conf = SparkConf()
    conf.setAppName("AppName")
    conf.setMaster('spark://localhost:7077')
    sc = None
    try:
        sc.stop()
        sc = SparkContext(conf=conf)
    except:
        sc = SparkContext(conf=conf)
    return sc

sc = spark_context_creator()
sc.setLogLevel("WARN")
