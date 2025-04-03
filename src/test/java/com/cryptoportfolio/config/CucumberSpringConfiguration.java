package com.cryptoportfolio.config;

import com.cryptoportfolio.service.TestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration(classes = TestConfig.class)
public class CucumberSpringConfiguration {
    // This class is intentionally empty
    // It's just a holder for the annotations
}