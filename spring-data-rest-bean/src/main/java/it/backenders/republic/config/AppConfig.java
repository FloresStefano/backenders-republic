package it.backenders.republic.config;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = { "it.backenders.republic.repo" })
@EnableTransactionManagement
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "it.backenders.republic", excludeFilters = {
		@ComponentScan.Filter(value = Controller.class, type = FilterType.ANNOTATION),
		@ComponentScan.Filter(value = Configuration.class, type = FilterType.ANNOTATION) })
public class AppConfig extends RepositoryRestMvcConfiguration {

	private static final Logger log = LoggerFactory.getLogger(AppConfig.class);




	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		super.configureRepositoryRestConfiguration(config);
		try {
			config.setBaseUri(new URI("/api"));
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		}

	}

	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder dsBilder = new EmbeddedDatabaseBuilder();
		dsBilder.setName("testdb;DATABASE_TO_UPPER=false");
		dsBilder.setType(EmbeddedDatabaseType.H2);
		DataSource out = dsBilder.build();
		return out;

	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws ClassNotFoundException {

		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		DataSource dataSource = dataSource();
		HibernateJpaVendorAdapter adapter = jpaVendorAdapter();
		Properties props = jpaProperties();

		adapter.setDatabase(Database.H2);
		props.put(Environment.HBM2DDL_AUTO, "create-drop");

		factoryBean.setDataSource(dataSource);
		factoryBean.setPackagesToScan("it.backenders.republic");
		factoryBean.setJpaVendorAdapter(adapter);
		factoryBean.setJpaProperties(props);
		factoryBean.afterPropertiesSet();

		return factoryBean;

	}

	@Bean
	public HibernateJpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setShowSql(true);
		adapter.setGenerateDdl(true);
		adapter.setDatabase(Database.H2);
		return adapter;
	}

	@Bean
	public JpaTransactionManager transactionManager() throws ClassNotFoundException {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	@Bean
	public Properties jpaProperties() {
		// validate: validate the schema, makes no changes to the database.
		// update: update the schema.
		// create: creates the schema, destroying previous data. (DEFAULT)
		// create-drop: drop the schema at the end of the session
		String jpaProperty = "validate";

		log.info("AppConfig property used: " + jpaProperty);

		Properties properties = new Properties();
		properties.put(Environment.HBM2DDL_AUTO, jpaProperty);
		return properties;
	}

}
