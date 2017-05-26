package it.addvalue.performance.publishers;

import it.addvalue.performance.metrics.PerformanceMetrics;

import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.databind.util.ISO8601Utils;

public class LoggingMetricsPublisher implements MetricsPublisher {

	private static final TimeZone EUROPE_ROME_TIMEZONE = TimeZone
			.getTimeZone("Europe/Rome");
	private Log log = LogFactory.getLog(LoggingMetricsPublisher.class);

	public void setLoggerCategory(String category) {
		log = LogFactory.getLog(category);
	}

	@Override
	public void publish(PerformanceMetrics metrics) {
		StringBuffer sb = new StringBuffer();

		sb.append("Job=")
				.append(metrics.overallStep.stepExecution.getJobExecution()
						.getJobInstance().getJobName()).append(" ");
		sb.append("Step=")
				.append(metrics.overallStep.stepExecution.getStepName())
				.append(" ");
		sb.append("JobInstanceId=")
				.append(metrics.overallStep.stepExecution.getJobExecution()
						.getJobInstance().getId()).append(" ");
		sb.append("JobExecutionId=")
				.append(metrics.overallStep.stepExecution.getJobExecutionId())
				.append(" ");
		sb.append("StepExecutionId=")
				.append(metrics.overallStep.stepExecution.getId()).append(" ");

		sb.append("TotalItemCount=").append(metrics.overallStep.totalItemCount)
				.append(" ");
		sb.append("PercentItemCount=")
				.append(metrics.overallStep.percentItemCount).append(" ");
		sb.append("ChunkItemsPerSecond=")
				.append(metrics.lastChunk.itemsPerSecond).append(" ");
		sb.append("StepItemsPerSecond=")
				.append(metrics.overallStep.itemsPerSecond).append(" ");
		sb.append("StartTime=").append(iso8601(metrics.overallStep.startTime))
				.append(" ");
		sb.append("EstimatedStepEndTime=")
				.append(iso8601(metrics.overallStep.estimatedEndTime))
				.append(" ");
		sb.append("IsLastChunk=").append(metrics.overallStep.isLastChunk);

		log.info(sb);
	}

	private static String iso8601(Date date) {
		return ISO8601Utils.format(date, false, EUROPE_ROME_TIMEZONE);
	}

}
