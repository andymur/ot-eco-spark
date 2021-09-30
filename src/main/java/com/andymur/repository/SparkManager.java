package com.andymur.repository;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Component;

@Component
public class SparkManager
{
    private final SparkSession sparkSession;

    public SparkManager() {
        SparkConf conf = new SparkConf()
            .setMaster("local[*]")
            .setAppName("OTUS");

        sparkSession = SparkSession
            .builder()
            .config(conf)
            .getOrCreate();
    }


    public SparkSession getSparkSession()
    {
        return sparkSession;
    }

    // TODO: extract settings
    // TODO: clusterize me
    public Dataset<Row> readTable(String tableName) {
        return getSparkSession().read()
            .format("jdbc")
            .option("url", "jdbc:postgresql://localhost/otus")
            .option("dbtable", tableName)
            .option("user", "otus")
            .option("password", "otus")
            .load();
    }
}
