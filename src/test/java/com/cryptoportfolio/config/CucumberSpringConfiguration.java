package com.cryptoportfolio.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration(classes = TestConfig.class)
public class CucumberSpringConfiguration {
    // This class is intentionally empty
    // Its purpose is to serve as a holder for the annotations
}