package com.andymur.pg.influxdb.meters.influx;

import org.influxdb.dto.Point;

public interface SinglePointSupplier {
    Point getPoint();
}
