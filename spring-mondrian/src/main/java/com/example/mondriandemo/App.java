package com.example.mondriandemo;

import com.example.mondriandemo.data.MondrianClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * Hello world!
 * https://mondrian.pentaho.com/documentation/olap.php
 */

@Controller
@EnableAutoConfiguration
public class App {


    @Autowired
    private MondrianClient mondrianClient;



    @RequestMapping("/")
    @ResponseBody
    public String index() {
        mondrianClient.runQuery();
        return "Will vc data services here\n";
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder dsBilder = new EmbeddedDatabaseBuilder();
        dsBilder.setName("testdb;DATABASE_TO_UPPER=false");
        dsBilder.setType(EmbeddedDatabaseType.H2);
        dsBilder.addScript("setupdb.sql");
        return dsBilder.build();
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication();
        app.setSources(Collections.<Object>singleton("com.example.mondriandemo"));
        app.run(args);
    }
}
