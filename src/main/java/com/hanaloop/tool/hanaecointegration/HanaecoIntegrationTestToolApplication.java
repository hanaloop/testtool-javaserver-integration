package com.hanaloop.tool.hanaecointegration;

import com.hanaloop.tool.hanaecointegration.config.HanaecoPropertyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

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

	@Bean
	RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

}
