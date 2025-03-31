package com.cryptoportfolio.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.cryptoportfolio.config.DatabaseConfig;

@Configuration
@ComponentScan("com.cryptoportfolio")
@Import(DatabaseConfig.class)
public class TestConfig {
}