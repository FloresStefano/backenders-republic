package com.example.mondriandemo;

import com.example.mondriandemo.data.MondrianClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * A manually-run test that covers a Mondrian query end-to-end
 */
@Configuration
@EnableAutoConfiguration
public class IntegrationTest implements CommandLineRunner {

    @Autowired
    private MondrianClient mondrianClient;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication();
        app.setWebEnvironment(false);
        app.setSources(Collections.<Object>singleton("com.example.mondriandemo"));
        app.run(args);
    }

    public void run(String... args) throws Exception {
        mondrianClient.runQuery();
    }
}
