package it.addvalue.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.Random;

public class TimeConsumingItemProcessor<T> implements ItemProcessor<T, T> {

    private static final Log log = LogFactory.getLog(TimeConsumingItemProcessor.class);

    private interface DelayStrategy {
        long delay();
    }

    private final DelayStrategy delayStrategy;

    private TimeConsumingItemProcessor(DelayStrategy delayStrategy) {
        this.delayStrategy = delayStrategy;
    }

    public static TimeConsumingItemProcessor<?> createWithFixedDelay(final long fixedDelay) {
        return new TimeConsumingItemProcessor<Object>(new DelayStrategy() {

            @Override
            public long delay() {
                return fixedDelay;
            }

        });
    }

    public static TimeConsumingItemProcessor<?> createWithRandomDelay(final long minimumDelay, final long maximumDelay) {
        return new TimeConsumingItemProcessor<Object>(new DelayStrategy() {

            private final Random random = new Random();

            @Override
            public long delay() {
                return minimumDelay + (long) (random.nextDouble() * (maximumDelay - minimumDelay));
            }

        });
    }

    @Override
    public T process(T item) throws Exception {
        long delay = delayStrategy.delay();
        Thread.sleep(delay);

        log.info("Processing took " + delay + " ms");

        return item;
    }

}
