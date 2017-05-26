package it.addvalue.performance.metrics;

import java.util.Date;

public class PerformanceMetrics {
    public Date currentTime;
    public StepMetrics overallStep = new StepMetrics();
    public ChunkMetrics lastChunk = new ChunkMetrics();
}
