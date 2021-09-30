package com.andymur;

import com.andymur.model.AggregationValue;
import com.andymur.model.CityRecord;
import com.andymur.model.CountryRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.*;

import java.util.*;
import java.util.stream.Collectors;

public class SparkJobRunner {

    public static void main(String[] args) {
        /*final List<AggregationValue> result = work(
            201902,
            202002,
            null,
            null,
            new HashSet<>(Arrays.asList("sql", "java"))
        );

        System.out.println("Result: " + result);*/

        System.out.println(cities());
    }

    private static List<CountryRecord> countries() {
        SparkConf conf = new SparkConf()
            .setMaster("local[*]")
            .setAppName("OTUS");

        SparkSession spark = SparkSession
            .builder()
            .config(conf)
            .getOrCreate();

        final Row[] rows = (Row[]) spark.read()
            .format("jdbc")
            .option("url", "jdbc:postgresql://localhost/otus")
            .option("dbtable", "lands")
            .option("user", "otus")
            .option("password", "otus")
            .load().collect();

        return Arrays.stream(rows).map(r -> new CountryRecord(r.getInt(0), r.getString(1))).collect(Collectors.toList());
    }

    private static List<CityRecord> cities() {
        SparkConf conf = new SparkConf()
            .setMaster("local[*]")
            .setAppName("OTUS");

        SparkSession spark = SparkSession
            .builder()
            .config(conf)
            .getOrCreate();

        final Row[] rows = (Row[]) spark.read()
            .format("jdbc")
            .option("url", "jdbc:postgresql://localhost/otus")
            .option("dbtable", "cities")
            .option("user", "otus")
            .option("password", "otus")
            .load().collect();

        return Arrays.stream(rows).map(r -> new CityRecord(r.getInt(0), r.getInt(2), r.getString(1))).collect(Collectors.toList());
    }

    private static List<AggregationValue> work(Integer fromPeriod, Integer toPeriod,
        Integer cityId, Integer countryId,
        Set<String> tags) {

        SparkConf conf = new SparkConf()
                .setMaster("local[*]")
                .setAppName("OTUS");

        SparkSession spark = SparkSession
                .builder()
                .config(conf)
                .getOrCreate();

        final Dataset<Row> ds = spark.read()
            .format("jdbc")
            .option("url", "jdbc:postgresql://localhost/otus")
            .option("dbtable", "posts")
            .option("user", "otus")
            .option("password", "otus")
            .load();

        Dataset<Row> ds1 = ds;

        if (fromPeriod != null) {
            ds1 = ds1.filter("posted_at >= " + fromPeriod);
        }

        if (toPeriod != null) {
            ds1 = ds1.filter("posted_at <=" + toPeriod);
        }

        if (cityId != null) {
            ds1 = ds1.filter("city_id = " + cityId);
        } else if (countryId != null) {
            ds1 = ds1.filter("land_id = " + countryId);
        }

        for (String tag: tags) {
            ds1 = ds1.filter(functions.array_contains(new Column("tags"), String.format("'%s'", tag)));
        }

        final Row[] rows = (Row[]) ds1.groupBy("posted_at")
            .count()
            .sort("posted_at")
            .collect();

        List<AggregationValue> result = new ArrayList<>();
        for (Row row: rows) {
            final String postedAt = Integer.toString((Integer) row.get(0));
            final Long count = (Long) row.get(1);
            final Integer year = Integer.valueOf(postedAt.substring(0, 4));
            final Integer month = Integer.valueOf(postedAt.substring(4, 6));
            result.add(new AggregationValue(year, month, count));
        }
        return result;
    }
}
