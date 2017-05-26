package it.addvalue.performance.metrics;

import org.springframework.batch.core.scope.context.ChunkContext;

import java.math.BigDecimal;

public class ChunkMetrics {
    public long count;
    public long millisAtStart;
    public long millisAtEnd;
    public long durationMillis;
    public long itemCountBefore;
    public long itemCountAfter;
    public long itemCountDelta;
    public BigDecimal itemsPerSecond;
    public ChunkContext chunkContext;
}
