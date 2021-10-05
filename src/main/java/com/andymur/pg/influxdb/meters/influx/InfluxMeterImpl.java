package com.andymur.pg.influxdb.meters.influx;

import org.influxdb.dto.Point;

import java.util.Map;

public abstract class InfluxMeterImpl<T> implements InfluxMeter<T> {

    protected final Map<String, String> tagSet;
    protected final String measurement;

    public InfluxMeterImpl(final String measurement,
                           final Map<String, String> tagSet) {
        this.measurement = measurement;
        this.tagSet = tagSet;
    }

    protected Point.Builder prepareBuilder() {
        final Point.Builder measurementBuilder = Point.measurement(measurement);
        tagSet.forEach(measurementBuilder::tag);
        return measurementBuilder;
    }

}
