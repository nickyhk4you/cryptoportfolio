package com.cryptoportfolio.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "com.cryptoportfolio")
@Import(DatabaseConfig.class)
public class AppConfig {
}