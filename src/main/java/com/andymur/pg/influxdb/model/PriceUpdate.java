package com.andymur.pg.influxdb.model;

public class PriceUpdate {
    private final String currencyCouple;
    private final long midValue;
    private final long spreadValue;
    private final int bandSize;

    public PriceUpdate(final String currencyCouple,
                       final long midValue,
                       final long spreadValue,
                       final int bandSize) {
        this.currencyCouple = currencyCouple;
        this.midValue = midValue;
        this.spreadValue = spreadValue;
        this.bandSize = bandSize;
    }

    public String getCurrencyCouple() {
        return currencyCouple;
    }

    public BandValue getBandValue() {
        return new BandValue(bandSize, midValue, spreadValue);
    }

    @Override
    public String toString() {
        return "PriceUpdate{" +
                "currencyCouple='" + currencyCouple + '\'' +
                ", midValue=" + midValue +
                ", spreadValue=" + spreadValue +
                '}';
    }
}
