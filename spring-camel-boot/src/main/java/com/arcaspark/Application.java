/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arcaspark;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.hibernate.cfg.Environment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@SpringBootApplication
@ComponentScan(basePackages={"com.arcaspark"})
public class Application {

    /**
     * Main method to start the application.
     * http://localhost:8080/camel/users/1
     * http://localhost:8080/camel/api-doc
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletRegistrationBean camelServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), "/camel/*");
        registration.setName("CamelServlet");
        return registration;
    }


    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder dsBilder = new EmbeddedDatabaseBuilder();
        dsBilder.setName("testdb;DATABASE_TO_UPPER=false");
        dsBilder.setType(EmbeddedDatabaseType.H2);
        return dsBilder.build();

    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws ClassNotFoundException {

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("com.arcaspark.model");
        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
        factoryBean.setJpaProperties(jpaProperties());
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
        Properties properties = new Properties();
        properties.put(Environment.HBM2DDL_AUTO, "create-drop");
        return properties;
    }

}
