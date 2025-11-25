package com.hanaloop.tool.hanaeco_integration_test_tool;

import com.hanaloop.tool.hanaeco_integration_test_tool.config.HanaecoPropertyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HanaecoIntegrationTestToolApplication {

	private static final Logger log = LoggerFactory.getLogger(HanaecoIntegrationTestToolApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(HanaecoIntegrationTestToolApplication.class, args);
	}

	@Bean
	CommandLineRunner logConfiguredBaseUrl(HanaecoPropertyProvider propertyProvider) {
		return args -> log.info("Hanaeco base URL configured as: {}", propertyProvider.getHanaecoBaseUrl());
	}

}
