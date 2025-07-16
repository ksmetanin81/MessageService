package com.colvir.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class CbsDataSourceConfig {

    @Value("${cbs.datasource.url}")
    private String url;
    @Value("${cbs.datasource.username}")
    private String username;
    @Value("${cbs.datasource.password}")
    private String password;
    @Value("${cbs.datasource.driver-class-name}")
    private String driver;

    private DataSource cbsDataSource() {
        return DataSourceBuilder
                .create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driver)
                .build();
    }

    @Bean
    public JdbcTemplate cbsJdbcTemplate() {
        return new JdbcTemplate(cbsDataSource());
    }
}
