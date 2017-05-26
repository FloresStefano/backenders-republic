package it.addvalue.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;

public class LoggingItemProcessor<T> implements ItemProcessor<T, T> {

    private static final Log log = LogFactory.getLog(LoggingItemProcessor.class);

    @Override
    public T process(T item) throws Exception {
        log.info("Processing item: " + item);
        return item;
    }

}
