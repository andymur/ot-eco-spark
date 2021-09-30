package com.andymur.repository;

import com.andymur.model.AggregationValue;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AggregatorRepository
{
    @Autowired
    private SparkManager sparkManager;

    public List<AggregationValue> calculate(Integer fromPeriod, Integer toPeriod,
        Integer cityId, Integer countryId,
        Set<String> tags) {

        final Dataset<Row> ds = sparkManager.readTable("posts");

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

        return Arrays.stream(rows)
            .map(AggregatorRepository::convertRowToAggregator)
            .collect(Collectors.toList());
    }

    private static AggregationValue convertRowToAggregator(Row row) {
        final String postedAt = Integer.toString((Integer) row.get(0));
        final Long count = (Long) row.get(1);
        final Integer year = Integer.valueOf(postedAt.substring(0, 4));
        final Integer month = Integer.valueOf(postedAt.substring(4, 6));
        return new AggregationValue(year, month, count);
    }
}
