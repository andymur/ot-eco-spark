package com.andymur.pg.influxdb.workers;

import com.andymur.pg.influxdb.Generator;
import com.andymur.pg.influxdb.model.PriceUpdate;

import java.util.concurrent.BlockingQueue;

public class UpdatesSupplier implements Runnable {

    private final Generator<PriceUpdate> updateGenerator;
    private final BlockingQueue<PriceUpdate> updatesQ;

    public UpdatesSupplier(final Generator<PriceUpdate> updateGenerator,
                           final BlockingQueue<PriceUpdate> updatesQ) {
        this.updateGenerator = updateGenerator;
        this.updatesQ = updatesQ;
    }

    @Override
    public void run() {
        try {
            while (updatesQ.offer(updateGenerator.generate())) {
                Thread.sleep(100L);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
