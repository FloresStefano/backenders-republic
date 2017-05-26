package it.addvalue.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class LoggingItemWriter<T> implements ItemWriter<T> {

    private static final Log log = LogFactory.getLog(LoggingItemWriter.class);

    @Override
    public void write(List<? extends T> list) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("Received chunk:");
        for (T item : list) {
            sb.append(item).append("\n");
        }
        log.info(sb);
    }

}
