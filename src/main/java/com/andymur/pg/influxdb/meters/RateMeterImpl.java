package com.andymur.pg.influxdb.meters;

import com.andymur.pg.influxdb.model.PriceUpdate;

import java.util.concurrent.atomic.AtomicLong;

public class RateMeterImpl implements Meter<PriceUpdate, Long> {

    private AtomicLong value = new AtomicLong();

    public RateMeterImpl() {
    }

    @Override
    public Long getValue() {
        return value.get();
    }

    @Override
    public void reset() {
        value.set(0L);
    }

    @Override
    public void process(PriceUpdate updateValue) {
        count();
    }

    private void count() {
        value.incrementAndGet();
    }

    @Override
    public boolean hasUpdates() {
        return value.get() > 0L;
    }
}
