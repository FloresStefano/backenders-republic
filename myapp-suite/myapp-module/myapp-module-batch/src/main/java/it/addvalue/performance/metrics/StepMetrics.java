package it.addvalue.performance.metrics;

import org.springframework.batch.core.StepExecution;

import java.math.BigDecimal;
import java.util.Date;

public class StepMetrics {
    public final ChunkMetrics lastChunk = new ChunkMetrics();
    public long millisFromStart;
    public long millisToEstimatedEnd;
    public Date startTime;
    public Date estimatedEndTime;
    public long totalItemCount;
    public long currentItemCount;
    public BigDecimal percentItemCount;
    public BigDecimal itemsPerSecond;
    public boolean isLastChunk;
    public StepExecution stepExecution;
}
