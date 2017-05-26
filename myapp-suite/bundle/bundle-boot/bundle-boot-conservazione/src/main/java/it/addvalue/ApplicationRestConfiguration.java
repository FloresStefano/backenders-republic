package it.addvalue;

import it.addvalue.entity.Document;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class ApplicationRestConfiguration extends RepositoryRestConfigurerAdapter
{

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config)
    {

        config.exposeIdsFor(Document.class);
        config.setBasePath("/api");

    }

}