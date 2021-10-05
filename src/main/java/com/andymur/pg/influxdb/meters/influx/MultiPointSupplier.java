package com.andymur.pg.influxdb.meters.influx;

import org.influxdb.dto.Point;

import java.util.List;

public interface MultiPointSupplier {
    List<Point> getPoints();
}
