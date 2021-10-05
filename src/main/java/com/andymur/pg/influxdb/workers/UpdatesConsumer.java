package com.andymur.pg.influxdb.workers;

import com.andymur.pg.influxdb.model.PriceUpdate;
import com.andymur.pg.influxdb.repository.MetersRepository;

import java.util.concurrent.BlockingQueue;

public class UpdatesConsumer implements Runnable {

    private final BlockingQueue<PriceUpdate> updatesQ;
    private final MetersRepository meterRepository;

    public UpdatesConsumer(final BlockingQueue<PriceUpdate> updatesQ,
                           final MetersRepository meterRepository) {
        this.updatesQ = updatesQ;
        this.meterRepository = meterRepository;
    }

    @Override
    public void run() {
        try {
            PriceUpdate priceUpdate;
            while (( priceUpdate = updatesQ.poll()) != null) {
                System.out.println(String.format("Got an update from supplier: %s", priceUpdate));
                meterRepository.processUpdate(priceUpdate);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
