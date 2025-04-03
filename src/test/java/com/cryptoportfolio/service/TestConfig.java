package com.cryptoportfolio.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;
import java.util.UUID;

@Configuration
@ComponentScan("com.cryptoportfolio")
public class TestConfig {
    
    @Bean
    public DataSource dataSource() {
        String dbName = "testdb_" + UUID.randomUUID().toString().replace("-", "");
        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .setName(dbName + ";MODE=MySQL")
            .addScript("classpath:schema.sql")
            .addScript("classpath:data.sql")
            .build();
    }
}