package com.andymur.pg.influxdb;

import com.andymur.pg.influxdb.meters.RateMeterImpl;
import com.andymur.pg.influxdb.meters.influx.InfluxRateMeterImpl;
import com.andymur.pg.influxdb.model.PriceUpdate;
import com.andymur.pg.influxdb.repository.Config;
import com.andymur.pg.influxdb.repository.InfluxRepositoryImpl;
import com.andymur.pg.influxdb.repository.MetersRepository;
import com.andymur.pg.influxdb.workers.MeterWorker;
import com.andymur.pg.influxdb.workers.UpdatesConsumer;
import com.andymur.pg.influxdb.workers.UpdatesSupplier;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;

import java.util.concurrent.*;

import static com.andymur.pg.influxdb.repository.MetersRepository.UPDATE_RATES_MEASUREMENT_NAME;

public class MeterSystemRunner {

    public static final String HOST = "localhost";

    private static final String DB_NAME = "test_measurements";
    private static final String RETENTION_POLICY = "autogen";

    private static final Config DIRECT_CONFIG = new Config(String.format("http://%s:8086", HOST), DB_NAME, RETENTION_POLICY);

    private static final Config TELEGRAF_CONFIG = new Config(String.format("http://%s:8087", HOST), DB_NAME, RETENTION_POLICY);

    public static void main(String[] args) {

        final PriceUpdateGenerator updateGenerator = new PriceUpdateGenerator();
        final BlockingQueue<PriceUpdate> updatesQ = new ArrayBlockingQueue<>(100_000);

        final UpdatesSupplier updatesSupplier = new UpdatesSupplier(updateGenerator, updatesQ);

        final MetersRepository metersRepository = new MetersRepository();
        metersRepository.addMeter(new InfluxRateMeterImpl(UPDATE_RATES_MEASUREMENT_NAME, new RateMeterImpl(),
                MetersRepository.defaultTagSet()));

        final UpdatesConsumer updatesConsumer = new UpdatesConsumer(updatesQ, metersRepository);

        final InfluxRepositoryImpl influxRepository = new InfluxRepositoryImpl(
                DIRECT_CONFIG
        );

        final MeterWorker meterWorker = new MeterWorker(metersRepository, influxRepository);

        final ExecutorService priceUpdateSupplierExecutor = Executors.newSingleThreadExecutor();
        final ScheduledExecutorService priceUpdateConsumerExecutor = Executors.newSingleThreadScheduledExecutor();
        final ScheduledExecutorService meterRepositoryExecutor = Executors.newSingleThreadScheduledExecutor();

        priceUpdateSupplierExecutor.submit(updatesSupplier);
        priceUpdateConsumerExecutor.scheduleWithFixedDelay(updatesConsumer, 0L, 10, TimeUnit.MILLISECONDS);
        meterRepositoryExecutor.scheduleWithFixedDelay(meterWorker, 0L, 1, TimeUnit.SECONDS);
    }

    private static InfluxDB getInfluxInstance(String host, String port) {
        InfluxDB influxDB = InfluxDBFactory.connect(String.format("http://%s:%s", host, port));
        Pong pong = influxDB.ping();
        if (pong.isGood()) {
            return influxDB;
        }
        throw new IllegalStateException("Failed to establish connection to influx db");
    }
}
