package com.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** AppConfig */
@Configuration
public class AppConfig {

    @Bean
    SimpleBean simpleBeanConfigure() {
        return new SimpleBean("toan");
    }

    @Bean("mysqlConnector")
    DatabaseConnector mysqlConnector(SimpleBean simpleBean) {
        DatabaseConnector mysqlConnector = new MysqlConnector();
        mysqlConnector.setUrl("jdbc:mysql://host1:33060/" + simpleBean.getUsername());
        return mysqlConnector;
    }

    @Bean("mongodbConnector")
    DatabaseConnector mongodbConfigure() {
        DatabaseConnector mongoDbConnector = new MongoDbConnector();
        mongoDbConnector.setUrl("mongodb://mongodb0.example.com:27017/toan");
        // Set username, password, format, v.v...
        return mongoDbConnector;
    }

    @Bean("postgresqlConnector")
    DatabaseConnector postgresqlConfigure() {
        DatabaseConnector postgreSqlConnector = new PostgreSqlConnector();
        postgreSqlConnector.setUrl("postgresql://localhost/toan");
        // Set username, password, format, v.v...
        return postgreSqlConnector;
    }
}
