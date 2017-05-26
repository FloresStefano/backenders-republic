/*
 * Copyright 2014-2016 the original author or authors. Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package it.addvalue;

import it.addvalue.entity.Address;
import it.addvalue.entity.Document;
import it.addvalue.entity.Item;
import it.addvalue.repository.DocumentRepository;
import it.addvalue.repository.ItemRepository;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * This example shows various ways to secure Spring Data REST applications using
 * Spring Security
 *
 * @author Greg Turnquist
 */

@SpringBootApplication
public class Application {
	@Autowired
	ItemRepository itemRepository;

	@Autowired
	DocumentRepository documentRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	/**
	 * Pre-load the system with employees and items.
	 */
	public @PostConstruct void init() {

		documentRepository.save(new Document("prova1"));
		/**
		 * Due to method-level protections on
		 * {@link example.company.ItemRepository}, the security context must be
		 * loaded with an authentication token containing the necessary
		 * privileges.
		 */
		  SecurityUtils.runAs("system", "system", "ROLE_ADMIN");
		itemRepository
				.save(new Item("Sting", new Address("via morgagni", "23")));
		itemRepository.save(new Item("the one ring", new Address(
				"Via del popolo", "22A")));

		SecurityContextHolder.clearContext();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**").allowedOrigins(
						"http://localhost:5453", "http://localhost:9100");
			}
		};
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		factory.setPort(9101);
		factory.setSessionTimeout(10, TimeUnit.MINUTES);

		return factory;
	}

}
