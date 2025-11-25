package com.hanaloop.tool.hanaecointegration.client;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * Stores the currently configured Hanaeco client details in-memory for reuse across requests.
 */
@Component
public class HanaecoClientContext {

	private volatile HanaecoClientDetails details;

	public synchronized HanaecoClientDetails configure(String baseUrl, String apiKey) {
		if (!StringUtils.hasText(baseUrl)) {
			throw new IllegalArgumentException("Base URL must not be blank");
		}
		if (!StringUtils.hasText(apiKey)) {
			throw new IllegalArgumentException("API key must not be blank");
		}
		String normalizedBaseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
		this.details = new HanaecoClientDetails(normalizedBaseUrl, apiKey);
		return this.details;
	}

	public Optional<HanaecoClientDetails> current() {
		return Optional.ofNullable(details);
	}
}
