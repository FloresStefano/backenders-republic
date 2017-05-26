package it.addvalue.performance.publishers;

import it.addvalue.performance.metrics.PerformanceMetrics;
import org.springframework.beans.factory.annotation.Required;

import javax.sql.DataSource;

public class DatabasePolipoMetricsPublisher extends LoggingMetricsPublisher {

    @Required
    public void setDataSource(DataSource dataSource) {
    }

    @Override
    public void publish(PerformanceMetrics metrics) {
        // TODO implementare la scrittura su DB
        super.publish(metrics);
    }
}
