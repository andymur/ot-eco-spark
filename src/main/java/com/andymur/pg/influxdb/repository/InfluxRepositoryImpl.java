package com.andymur.pg.influxdb.repository;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;

public class InfluxRepositoryImpl implements InfluxRepository {

    private final InfluxDB influxInstance;
    private final Config config;

    public InfluxRepositoryImpl(final Config config) {
        this.config = config;
        this.influxInstance = connect();
    }

    @Override
    public void writePoint(final Point point) {
        influxInstance.write(config.getDatabaseName(), config.getRetentionPolicy(), point);
    }

    private InfluxDB connect() {
        InfluxDB influxDB = InfluxDBFactory.connect(config.getUrl());
        Pong pong = influxDB.ping();
        if (pong.isGood()) {
            return influxDB;
        }
        throw new IllegalStateException("Failed to establish connection to influx db");
    }
}
