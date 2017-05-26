package it.addvalue.performance;

import it.addvalue.performance.counters.FileLineCounter;
import it.addvalue.performance.counters.ItemCounter;
import it.addvalue.performance.counters.QueryItemCounter;
import it.addvalue.performance.metrics.PerformanceMetrics;
import it.addvalue.performance.publishers.LoggingMetricsPublisher;
import it.addvalue.performance.publishers.MetricsPublisher;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class PerformanceMonitor implements InitializingBean, StepExecutionListener, ChunkListener {

    private static final MetricsPublisher DEFAULT_METRICS_PUBLISHER = new LoggingMetricsPublisher();

    private ItemCounter inputItemsCountingStrategy;
    private MetricsPublisher metricsPublishingStrategy = DEFAULT_METRICS_PUBLISHER;
    private long totalItemCount;
    private long millisAtStepStart;
    private long millisAtChunkStart;
    private Date stepStartTime;
    private long itemCountAtChunkStart;
    private long chunkCount;
    private StepExecution stepExecution;

    public static PerformanceMonitor createByResource(Resource file) {
        PerformanceMonitor monitor = new PerformanceMonitor();
        monitor.setInputItemsCountingStrategy(new FileLineCounter(file));
        return monitor;
    }

    public static PerformanceMonitor createByQuery(DataSource dataSource, String sql) {
        return createByQuery(dataSource, sql, new Object[]{});
    }

    public static PerformanceMonitor createByQuery(DataSource dataSource, String sql, Object... args) {
        PerformanceMonitor monitor = new PerformanceMonitor();
        monitor.setInputItemsCountingStrategy(new QueryItemCounter(dataSource, sql, args));
        return monitor;
    }

    public void setInputItemsCountingStrategy(ItemCounter inputItemsCountingStrategy) {
        this.inputItemsCountingStrategy = inputItemsCountingStrategy;
    }

    public void setMetricsPublishingStrategy(MetricsPublisher metricsPublishingStrategy) {
        this.metricsPublishingStrategy = metricsPublishingStrategy;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(inputItemsCountingStrategy, "Property 'inputItemsCountingStrategy' must not be null.");
        Assert.notNull(metricsPublishingStrategy, "Property 'metricsPublishingStrategy' must not be null.");
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        millisAtStepStart = System.currentTimeMillis();
        stepStartTime = new Date(millisAtStepStart);
        totalItemCount = inputItemsCountingStrategy.itemCount();
        chunkCount = 0;
    }

    @Override
    public void beforeChunk(ChunkContext context) {
        millisAtChunkStart = System.currentTimeMillis();
        itemCountAtChunkStart = context.getStepContext().getStepExecution().getReadCount();
    }

    @Override
    public void afterChunk(ChunkContext context) {
        long millisAtChunkEnd = System.currentTimeMillis();
        long millisFromStepStart = millisAtChunkEnd - millisAtStepStart;
        long itemCountAtChunkEnd = context.getStepContext().getStepExecution().getReadCount();
        long chunkItemCountDelta = itemCountAtChunkEnd - itemCountAtChunkStart;
        long chunkDurationMillis = millisAtChunkEnd - millisAtChunkStart;

        PerformanceMetrics metrics = new PerformanceMetrics();

        metrics.currentTime = new Date(millisAtChunkEnd);
        metrics.overallStep.millisFromStart = millisFromStepStart;
        metrics.overallStep.startTime = stepStartTime;
        metrics.overallStep.totalItemCount = totalItemCount;
        metrics.overallStep.currentItemCount = itemCountAtChunkEnd;
        metrics.overallStep.percentItemCount = percentOfTotalItemCount(itemCountAtChunkEnd);
        metrics.overallStep.itemsPerSecond = itemsPerSecond(itemCountAtChunkEnd, millisFromStepStart);
        metrics.overallStep.stepExecution = stepExecution;
        metrics.overallStep.isLastChunk = (itemCountAtChunkEnd == totalItemCount);

        metrics.lastChunk.count = ++chunkCount;
        metrics.lastChunk.millisAtStart = millisAtChunkStart;
        metrics.lastChunk.millisAtEnd = millisAtChunkEnd;
        metrics.lastChunk.durationMillis = chunkDurationMillis;
        metrics.lastChunk.itemCountBefore = itemCountAtChunkStart;
        metrics.lastChunk.itemCountAfter = itemCountAtChunkEnd;
        metrics.lastChunk.itemCountDelta = chunkItemCountDelta;
        metrics.lastChunk.itemsPerSecond = itemsPerSecond(chunkItemCountDelta, chunkDurationMillis);
        metrics.lastChunk.chunkContext = context;

        long millisToEstimatedStepEnd = estimateRemainingMillis(metrics);
        metrics.overallStep.millisToEstimatedEnd = millisToEstimatedStepEnd;
        metrics.overallStep.estimatedEndTime = new Date(millisAtStepStart + millisToEstimatedStepEnd);

        metricsPublishingStrategy.publish(metrics);
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        // Nulla da fare qui
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // Nulla da fare qui
        return null;
    }

    private BigDecimal percentOfTotalItemCount(long itemCount) {
        double percent;
        if (totalItemCount == 0) {
            percent = 100d;
        } else {
            percent = 100d * itemCount / (double) totalItemCount;
        }
        return bigDecimal(percent);
    }

    private BigDecimal itemsPerSecond(long itemCount, long durationMillis) {
        if (durationMillis == 0) {
            durationMillis = 1;
        }
        return bigDecimal(1000d * itemCount / (double) durationMillis);
    }

    private long estimateRemainingMillis(PerformanceMetrics metrics) {
        float millisFromStart = metrics.overallStep.millisFromStart;
        float totalItemCount = metrics.overallStep.totalItemCount;
        float currentItemCount = metrics.overallStep.currentItemCount;
        return (long) (millisFromStart * totalItemCount / currentItemCount);
    }

    private BigDecimal bigDecimal(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
    }

}
