package com.hanaloop.tool.hanaecointegration.client;

/**
 * In-memory representation of the Hanaeco client configuration supplied by the UI.
 */
public record HanaecoClientDetails(String baseUrl, String apiKey) {
}
