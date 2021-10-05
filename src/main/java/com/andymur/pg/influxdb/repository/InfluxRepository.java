package com.andymur.pg.influxdb.repository;

import org.influxdb.dto.Point;

public interface InfluxRepository {
    void writePoint(final Point point);
}
