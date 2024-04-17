package com.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** AppConfig */
@Configuration
public class AppConfig {

    @Value("${toan.mysql.url}")
    String mysqlUrl;

    @Bean
    DatabaseConnector mysqlConfigure() {
        DatabaseConnector mysqlConnector = new MySqlConnector();

        System.out.println("Config Mysql Url: " + mysqlUrl);
        mysqlConnector.setUrl(mysqlUrl);

        return mysqlConnector;
    }
}
