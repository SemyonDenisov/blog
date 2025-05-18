package ru.yandex.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Configuration
@SpringJUnitConfig(classes = TestDataSourceConfiguration.class)
public class IntegrationTestConfig {

}