package it.addvalue.demo;

import org.springframework.batch.item.file.transform.LineAggregator;

public class UppercaseLineAggregator<T> implements LineAggregator<T> {

    @Override
    public String aggregate(T item) {
        return item.toString().toUpperCase();
    }

}
