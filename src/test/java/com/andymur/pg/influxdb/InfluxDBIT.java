package com.andymur.pg.influxdb;

import com.andymur.pg.influxdb.meters.CurrencyCoupleRateMeterImpl;
import com.andymur.pg.influxdb.meters.Meter;
import com.andymur.pg.influxdb.meters.RateMeterImpl;
import com.andymur.pg.influxdb.meters.influx.InfluxMeter;
import com.andymur.pg.influxdb.model.PriceUpdate;
import com.andymur.pg.influxdb.repository.InfluxRepository;
import com.andymur.pg.influxdb.repository.MetersRepository;
import com.andymur.pg.influxdb.workers.MeterWorker;
import com.andymur.pg.influxdb.workers.UpdatesConsumer;
import org.influxdb.dto.Point;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/*
 * kind of integration test
 */
public class InfluxDBIT {

    private Generator<PriceUpdate> generatorMock;

    private InfluxRepositoryMock influxRepository = new InfluxRepositoryMock();
    final MetersRepository metersRepository = new MetersRepository();

    private final BlockingQueue<PriceUpdate> updatesQ = new ArrayBlockingQueue<>(100);

    private MeterWorker meterWorker;
    private UpdatesConsumer updatesConsumer;

    @Before
    public void setUp() {
        meterWorker = new MeterWorker(metersRepository, influxRepository);
        updatesConsumer = new UpdatesConsumer(updatesQ, metersRepository);
    }

    @Test
    public void testRateMeasurements() {

        // we should have 4 rates per sec in general and 3, 1 for EURUSD, EURGBP ccy couples respectively according to the data below
        generatorMock = new PriceUpdateGeneratorMock(
                Arrays.asList(
                        createUpdate("EURUSD", 12000L, 10L, 1),
                        createUpdate("EURUSD", 11000L, 20L, 2),
                        createUpdate("EURUSD", 10000L, 5L, 1),
                        createUpdate("EURGBP", 8000L, 5L, 1)
                )
        );

        PriceUpdate update;

        while ((update = generatorMock.generate()) != null) {
            updatesQ.offer(update);
        }

        InfluxMeterMock generalRateMeter = new InfluxMeterMock(new RateMeterImpl());
        InfluxMeterMock eurUsdRateMeter = new InfluxMeterMock(new CurrencyCoupleRateMeterImpl("EURUSD"));
        InfluxMeterMock eurGbpRateMeter = new InfluxMeterMock(new CurrencyCoupleRateMeterImpl("EURGBP"));

        metersRepository.addMeter(generalRateMeter);
        metersRepository.addMeter(eurUsdRateMeter);
        metersRepository.addMeter(eurGbpRateMeter);

        updatesConsumer.run();
        meterWorker.run();

        Assert.assertEquals(4L, (long) generalRateMeter.meter.getValue());
        Assert.assertEquals(3L, (long) eurUsdRateMeter.meter.getValue());
        Assert.assertEquals(1L, (long) eurGbpRateMeter.meter.getValue());
    }

    private static PriceUpdate createUpdate(final String currencyCouple,
                                            final long midValue,
                                            final long spreadValue,
                                            final int bandSize) {
        return new PriceUpdate(currencyCouple, midValue, spreadValue, bandSize);
    }

    private static class InfluxMeterMock implements InfluxMeter<PriceUpdate> {

        private final Meter<PriceUpdate, Long> meter;

        public InfluxMeterMock(Meter<PriceUpdate, Long> meter) {
            this.meter = meter;
        }

        @Override
        public void process(PriceUpdate updateValue) {
            meter.process(updateValue);
        }

        @Override
        public void reset() {
            // do nothing
        }

        @Override
        public boolean hasUpdates() {
            return meter.hasUpdates();
        }
    }

    private static class InfluxRepositoryMock implements InfluxRepository {

        private int writes = 0;

        @Override
        public void writePoint(Point point) {
            writes += 1;
        }

        public int getWrites() {
            return writes;
        }
    }

    private static class PriceUpdateGeneratorMock implements Generator<PriceUpdate> {

        private final Iterator<PriceUpdate> sourceOfData;

        public PriceUpdateGeneratorMock(List<PriceUpdate> data) {
            sourceOfData = data.iterator();
        }

        @Override
        public PriceUpdate generate() {
            if (sourceOfData.hasNext()) {
                return sourceOfData.next();
            }
            return null;
        }
    }
}
