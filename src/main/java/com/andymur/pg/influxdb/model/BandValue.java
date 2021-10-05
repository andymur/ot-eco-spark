package com.andymur.pg.influxdb.model;

public class BandValue {
    private final int amount;
    private final long midValue;
    private final long spreadValue;

    public BandValue(int amount, long midValue, long spreadValue) {
        this.amount = amount;
        this.midValue = midValue;
        this.spreadValue = spreadValue;
    }

    public int getAmount() {
        return amount;
    }

    public long getMidValue() {
        return midValue;
    }

    public long getSpreadValue() {
        return spreadValue;
    }
}
