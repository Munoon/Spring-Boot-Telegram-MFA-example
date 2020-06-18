package com.habr.telegrambotmfa.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("heroku")
public class HerokuDatabaseConfig {
    private String dbUrl;

    public HerokuDatabaseConfig(@Value("${spring.datasource.url}") String dbUrl) {
        this.dbUrl = dbUrl;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        return new HikariDataSource(config);
    }
}
