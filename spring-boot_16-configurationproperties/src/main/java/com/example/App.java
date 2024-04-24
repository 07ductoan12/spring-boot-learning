package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/** App */
@SpringBootApplication
@EnableConfigurationProperties
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Autowired ToanAppProperties toanAppProperties;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Global variable:");
        System.out.println("\t Email: " + toanAppProperties.getEmail());
        System.out.println("\t GA ID: " + toanAppProperties.getGoogleAnalyticsId());
        System.out.println("\t Authors: " + toanAppProperties.getAuthors());
        System.out.println("\t Example Map: " + toanAppProperties.getExampleMap());
    }
}
