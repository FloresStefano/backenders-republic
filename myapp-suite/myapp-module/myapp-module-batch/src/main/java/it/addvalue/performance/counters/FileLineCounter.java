package it.addvalue.performance.counters;

import java.io.IOException;

import org.apache.commons.io.LineIterator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class FileLineCounter implements InitializingBean, ItemCounter {

	private final Resource resource;
	private String encoding = "UTF-8";

	public FileLineCounter(Resource resource) {
		this.resource = resource;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(encoding, "Property 'encoding' must not be null.");
	}

	@Override
	public long itemCount() {
		try {

			LineIterator lineIterator = org.apache.commons.io.FileUtils
					.lineIterator(resource.getFile(), encoding);
			long lines = 0;
			try {
				while (lineIterator.hasNext()) {
					lines++;
					lineIterator.nextLine();
				}
			} finally {
				LineIterator.closeQuietly(lineIterator);
			}
			return lines;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
