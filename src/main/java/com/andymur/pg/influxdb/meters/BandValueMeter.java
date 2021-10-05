package com.andymur.pg.influxdb.meters;

import com.andymur.pg.influxdb.model.BandValue;
import com.andymur.pg.influxdb.model.PriceUpdate;

public class BandValueMeter implements Meter<PriceUpdate, BandValue> {

    private long midValueAccum;
    private long spreadValueAccum;
    private int processedUpdates;

    private final String currencyCouple;
    private final int bandAmount;

    public BandValueMeter(final String currencyCouple,
                          final int bandAmount) {
        this.currencyCouple = currencyCouple;
        this.bandAmount = bandAmount;
    }

    @Override
    public synchronized BandValue getValue() {
        final long midValue = midValueAccum / processedUpdates;
        final long spreadValue = spreadValueAccum / processedUpdates;
        return new BandValue(bandAmount, midValue, spreadValue);
    }

    @Override
    public synchronized void reset() {
        midValueAccum = 0L;
        spreadValueAccum = 0L;
        processedUpdates = 0;
    }

    @Override
    public void process(PriceUpdate updateValue) {
        if (updateValue.getCurrencyCouple().equals(currencyCouple)
                && updateValue.getBandValue().getAmount() == bandAmount) {
            internalProcess(updateValue);
        }
    }

    private synchronized void internalProcess(PriceUpdate updateValue) {
        midValueAccum += updateValue.getBandValue().getMidValue();
        spreadValueAccum += updateValue.getBandValue().getSpreadValue();
        processedUpdates += 1;
    }

    @Override
    public boolean hasUpdates() {
        return processedUpdates > 0;
    }
}
