package com.andymur.pg.influxdb;

import com.andymur.pg.influxdb.model.PriceUpdate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PriceUpdateGenerator implements Generator<PriceUpdate> {

    private static final Random SOURCE_OF_RANDOMNESS = new Random(System.currentTimeMillis());

    private static List<String> CURRENCY_COUPLES_CACHE = Arrays.asList(
            "EURUSD", "EURRUB", "USDRUB", "EURGBP", "GBPUSD", "EURJPY", "EURCHF", "USDCHF", "EURCAD", "EURNOK"
    );

    private static List<Integer> BAND_AMOUNTS = Arrays.asList(500_000, 1000_000, 5000_000);

    @Override
    public PriceUpdate generate() {
        return new PriceUpdate(randomFromList(CURRENCY_COUPLES_CACHE),
                randomInt(10000, 110000),
                randomInt(100, 300),
                randomFromList(BAND_AMOUNTS));
    }

    static <E> E randomFromList(List<E> elements) {
        return elements.get(randomInt(0, elements.size() - 1));
    }

    static int randomInt(int from, int to) {
        return from + SOURCE_OF_RANDOMNESS.nextInt(to - from + 1);
    }
}
