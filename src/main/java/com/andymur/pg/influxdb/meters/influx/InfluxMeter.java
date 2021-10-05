package com.andymur.pg.influxdb.meters.influx;

public interface InfluxMeter<T> {
    void process(T updateValue);
    void reset();
    boolean hasUpdates();
}
