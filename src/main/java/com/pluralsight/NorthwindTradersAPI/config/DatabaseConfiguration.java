package com.pluralsight.NorthwindTradersAPI.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {

    private final BasicDataSource basicDataSource;

    @Bean
    public DataSource dataSource(){
        return basicDataSource;
    }

    @Autowired
    public DatabaseConfiguration(
            @Value("${connectionUrl}") String url,
            @Value("${username}") String username,
            @Value("${password}") String password) {

        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);

        this.basicDataSource = ds;
    }

}
