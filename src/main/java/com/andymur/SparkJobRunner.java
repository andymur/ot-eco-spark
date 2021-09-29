package com.andymur;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;
import scala.collection.mutable.WrappedArray;

public class SparkJobRunner {
    public static void main(String[] args) {
        work();
    }

    private static void work() {

        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("OTUS");

        SparkSession spark = SparkSession
                .builder()
                .config(conf)
                .getOrCreate();

        Dataset<Row> jdbcDF = spark.read()
                .format("jdbc")
                .option("url", "jdbc:postgresql://localhost/otus")
                .option("dbtable", "posts")
                .option("user", "otus")
                .option("password", "otus")
                .load();

        /*final Dataset<Row> tags = */jdbcDF
            //.filter("posted_at >= 202001")
            .filter(functions.array_contains(new Column("tags"), "'sql'"))
            .groupBy(new Column("posted_at")).count().show();
            //.filter(functions.array_contains(new Column("tags"), "'java'"));
        //.show(5);

        /*final Row[] take = (Row[]) jdbcDF.take(1);
        WrappedArray warr = (WrappedArray) take[0].get(8);
        System.out.println(warr);
        System.out.println(warr.contains("'sql'"));*/
        //System.out.println("number: " + tags.count());
    }
}
