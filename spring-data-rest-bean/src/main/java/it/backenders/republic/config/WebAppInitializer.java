package it.backenders.republic.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@Order(value = 1)
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(WebAppInitializer.class);

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { AppConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		super.onStartup(servletContext);

	}



	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}