package it.addvalue.performance.publishers;

import it.addvalue.performance.metrics.PerformanceMetrics;

public interface MetricsPublisher {

    void publish(PerformanceMetrics metrics);

}
