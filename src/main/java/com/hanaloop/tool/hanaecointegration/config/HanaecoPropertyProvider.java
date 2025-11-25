package com.hanaloop.tool.hanaecointegration.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Simple holder for Hanaeco-specific configuration that comes from application.yml.
 */
@Component
public class HanaecoPropertyProvider {

	private final String hanaecoBaseUrl;

	public HanaecoPropertyProvider(@Value("${hanaecoBaseUrl}") String hanaecoBaseUrl) {
		this.hanaecoBaseUrl = hanaecoBaseUrl;
	}

	public String getHanaecoBaseUrl() {
		return hanaecoBaseUrl;
	}
}
