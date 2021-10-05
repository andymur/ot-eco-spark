package com.andymur.pg.influxdb.meters.influx;

import com.andymur.pg.influxdb.meters.Meter;
import com.andymur.pg.influxdb.model.BandValue;
import com.andymur.pg.influxdb.model.PriceUpdate;
import org.influxdb.dto.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InfluxBandValueMeterImpl extends InfluxMeterImpl<PriceUpdate> implements MultiPointSupplier {

    private final static String MID_PRICE_VALUE_FIELD_NAME = "mid_price";
    private final static String SPREAD_VALUE_FIELD_NAME = "spread";

    private final List<Meter<PriceUpdate, BandValue>> meters;

    public InfluxBandValueMeterImpl(final String measurement,
                                    final Map<String, String> tagSet) {
        super(measurement, tagSet);
        meters = new ArrayList<>();
    }

    public void addBandValueMeter(final Meter<PriceUpdate, BandValue> meter) {
        this.meters.add(meter);
    }

    @Override
    public List<Point> getPoints() {
        return meters.stream().filter(Meter::hasUpdates).map(this::getPoint).collect(Collectors.toList());
    }

    private Point getPoint(Meter<PriceUpdate, BandValue> meter) {
        final BandValue value = meter.getValue();
        Point.Builder measurementBuilder = prepareBuilder();
        measurementBuilder.tag("band_size", String.valueOf(value.getAmount()));
        return measurementBuilder
                .addField(MID_PRICE_VALUE_FIELD_NAME, value.getMidValue())
                .addField(SPREAD_VALUE_FIELD_NAME, value.getSpreadValue())
                .build();
    }

    @Override
    public void process(PriceUpdate updateValue) {
        meters.forEach(meter -> meter.process(updateValue));
    }

    @Override
    public void reset() {
        meters.forEach(Meter::reset);
    }

    @Override
    public boolean hasUpdates() {
        return meters.stream().anyMatch(Meter::hasUpdates);
    }
}
