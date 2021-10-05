package com.andymur.pg.influxdb.meters.influx;

import com.andymur.pg.influxdb.meters.Meter;
import com.andymur.pg.influxdb.model.PriceUpdate;
import org.influxdb.dto.Point;

import java.util.Map;

public class InfluxRateMeterImpl extends InfluxMeterImpl<PriceUpdate> implements SinglePointSupplier {

    private final static String RATE_FIELD_NAME = "rate";
    private final Meter<PriceUpdate, Long> meter;

    public InfluxRateMeterImpl(final String measurement,
                               final Meter<PriceUpdate, Long> meter,
                               final Map<String, String> tagSet) {
        super(measurement, tagSet);
        this.meter = meter;
    }

    @Override
    public Point getPoint() {
        final Long value = meter.getValue();
        Point.Builder measurementBuilder = prepareBuilder();
        return measurementBuilder.addField(RATE_FIELD_NAME, value).build();
    }

    public void process(PriceUpdate updateValue) {
        meter.process(updateValue);
    }

    @Override
    public void reset() {
        meter.reset();
    }

    @Override
    public boolean hasUpdates() {
        return meter.hasUpdates();
    }
}
