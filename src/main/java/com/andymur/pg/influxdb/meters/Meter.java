package com.andymur.pg.influxdb.meters;

public interface Meter<T, V> {
    V getValue();
    void reset();
    void process(T updateValue);
    boolean hasUpdates();
}
