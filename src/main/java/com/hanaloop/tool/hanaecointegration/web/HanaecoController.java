package com.hanaloop.tool.hanaecointegration.web;

import com.hanaloop.tool.hanaecointegration.client.HanaecoClientContext;
import com.hanaloop.tool.hanaecointegration.client.HanaecoClientDetails;
import com.hanaloop.tool.hanaecointegration.config.HanaecoPropertyProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Controller
public class HanaecoController {

	private final HanaecoPropertyProvider propertyProvider;
	private final HanaecoClientContext clientContext;
	private final RestTemplate restTemplate;

	public HanaecoController(HanaecoPropertyProvider propertyProvider,
							 HanaecoClientContext clientContext,
							 RestTemplate restTemplate) {
		this.propertyProvider = propertyProvider;
		this.clientContext = clientContext;
		this.restTemplate = restTemplate;
	}

	@GetMapping("/")
	public String showLanding(Model model) {
		populateModel(model, null, null, null, null);
		return "main";
	}

	@PostMapping("/configure")
	public String configure(@RequestParam String baseUrl,
							@RequestParam String apiKey,
							Model model) {
		if (!StringUtils.hasText(baseUrl) || !StringUtils.hasText(apiKey)) {
			populateModel(model, null, "Base URL and API key are both required.", null, null);
			return "main";
		}

		HanaecoClientDetails details = clientContext.configure(baseUrl, apiKey);
		String message = "Configuration saved. Requests will target %s with the provided API key."
			.formatted(details.baseUrl());
		populateModel(model, message, null, null, null);
		return "main";
	}

	@PostMapping("/fetch-info")
	public String fetchInfo(Model model) {
		Optional<HanaecoClientDetails> detailsOptional = clientContext.current();
		if (detailsOptional.isEmpty()) {
			populateModel(model, null, null, null, "Configure the Hanaeco client before requesting info.");
			return "main";
		}

		HanaecoClientDetails details = detailsOptional.get();
		String uri = details.baseUrl() + "/info";
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-API-KEY", details.apiKey());

		try {
			ResponseEntity<String> response = restTemplate.exchange(
				uri,
				HttpMethod.GET,
				new HttpEntity<>(headers),
				String.class
			);
			String payload = response.getBody() != null ? response.getBody() : "<empty response>";
			populateModel(model, null, null, payload, null);
		} catch (RestClientResponseException ex) {
			String message = "Hanaeco /info request failed (%d): %s"
				.formatted(ex.getRawStatusCode(), ex.getResponseBodyAsString());
			populateModel(model, null, null, null, message);
		} catch (RestClientException ex) {
			populateModel(model, null, null, null, "Unable to reach Hanaeco server: " + ex.getMessage());
		}

		return "main";
	}

	private void populateModel(Model model,
							   String configurationMessage,
							   String configurationError,
							   String fetchResult,
							   String fetchError) {

		Optional<HanaecoClientDetails> current = clientContext.current();
		String defaultBaseUrl = propertyProvider.getHanaecoBaseUrl();
		String configuredBaseUrl = current.map(HanaecoClientDetails::baseUrl).orElse(defaultBaseUrl);

		model.addAttribute("baseUrlFieldValue", configuredBaseUrl);
		model.addAttribute("configuredBaseUrl", configuredBaseUrl);
		model.addAttribute("apiKeyFieldValue", "");
		model.addAttribute("configurationReady", current.isPresent());
		model.addAttribute("configurationMessage", configurationMessage);
		model.addAttribute("configurationError", configurationError);
		model.addAttribute("fetchResult", fetchResult);
		model.addAttribute("fetchError", fetchError);
	}
}
