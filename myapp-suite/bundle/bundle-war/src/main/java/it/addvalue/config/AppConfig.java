package it.addvalue.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages =
{ "it.addvalue.repository" })
@EnableTransactionManagement
@EnableAspectJAutoProxy
@ComponentScan(basePackages = "it.addvalue")
public class AppConfig extends RepositoryRestMvcConfiguration
{

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config)
    {
        config.setBasePath("/api");
    }

    @Bean
    public DataSource dataSource()
    {
        EmbeddedDatabaseBuilder dsBilder = new EmbeddedDatabaseBuilder();
        dsBilder.setName("testdb;DATABASE_TO_UPPER=false");
        dsBilder.setType(EmbeddedDatabaseType.H2);
        return dsBilder.build();

        // final JdbcDataSource dataSource = new JdbcDataSource();
        // // dataSource.set("org.hsqldb.jdbcDriver");
        // dataSource.setUrl("jdbc:h2:mem:play;MODE=MSSQLServer;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;DB_CLOSE_ON_EXIT=FALSE");
        // dataSource.setUser("sa");
        // dataSource.setPassword("");
        // return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws ClassNotFoundException
    {

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("it.addvalue.entity");
        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        factoryBean.setJpaProperties(jpaProperties());
        return factoryBean;

    }

    @Bean
    public HibernateJpaVendorAdapter jpaVendorAdapter()
    {
        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setShowSql(true);
        adapter.setGenerateDdl(true);
        adapter.setDatabase(Database.H2);
        return adapter;
    }

    @Bean
    public JpaTransactionManager transactionManager() throws ClassNotFoundException
    {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public Properties jpaProperties()
    {
        // validate: validate the schema, makes no changes to the database.
        // update: update the schema.
        // create: creates the schema, destroying previous data. (DEFAULT)
        // create-drop: drop the schema at the end of the session
        Properties properties = new Properties();
        properties.put(Environment.HBM2DDL_AUTO, "create-drop");
        return properties;
    }

}
