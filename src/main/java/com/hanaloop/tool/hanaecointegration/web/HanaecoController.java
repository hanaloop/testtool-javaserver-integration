package com.hanaloop.tool.hanaecointegration.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanaloop.hanaeco.model.OrganizationDto;
import com.hanaloop.hanaeco.model.ProductDto;
import com.hanaloop.tool.hanaecointegration.client.HanaecoClientContext;
import com.hanaloop.tool.hanaecointegration.client.HanaecoClientDetails;
import com.hanaloop.tool.hanaecointegration.client.OrganizationLookupService;
import com.hanaloop.tool.hanaecointegration.client.ProductLookupService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HanaecoController {

	private final HanaecoPropertyProvider propertyProvider;
	private final HanaecoClientContext clientContext;
	private final RestTemplate restTemplate;
	private final OrganizationLookupService organizationLookupService;
	private final ProductLookupService productLookupService;
	private final ObjectMapper objectMapper;

	public HanaecoController(HanaecoPropertyProvider propertyProvider,
							 HanaecoClientContext clientContext,
							 RestTemplate restTemplate,
							 OrganizationLookupService organizationLookupService,
							 ProductLookupService productLookupService,
							 ObjectMapper objectMapper) {
		this.propertyProvider = propertyProvider;
		this.clientContext = clientContext;
		this.restTemplate = restTemplate;
		this.organizationLookupService = organizationLookupService;
		this.productLookupService = productLookupService;
		this.objectMapper = objectMapper;
	}

	@GetMapping("/")
	public String showLanding(Model model) {
		populateModel(model, null, null, null, null);
		return "main";
	}

	@GetMapping("/organizations")
	public String showOrganizationLookup(Model model) {
		populateOrganizationModel(model, "", "", null, null, null);
		return "organization";

	}


	@GetMapping("/products")
	public String showProductsLookup(Model model) {
		populateProductModel(model, "", "", "", "", null, null, null);
		return "product";
	}

	@PostMapping("/organizations/query")
	public String queryOrganizations(@RequestParam(required = false) String organizationId,
									 @RequestParam(required = false) String organizationName,
									 Model model) {
		String organizationIdValue = organizationId != null ? organizationId.trim() : "";
		String organizationNameValue = organizationName != null ? organizationName.trim() : "";

		if (!StringUtils.hasText(organizationIdValue) && !StringUtils.hasText(organizationNameValue)) {
			populateOrganizationModel(model,
				organizationIdValue,
				organizationNameValue,
				null,
				"Provide either an organization ID or name before querying.",
				null);
			return "organization";
		}

		try {
			List<OrganizationDto> organizations = organizationLookupService.fetchOrganizations(organizationIdValue, organizationNameValue);
			String criteriaDescription = buildOrganizationCriteriaDescription(organizationIdValue, organizationNameValue);
			if (organizations.isEmpty()) {
				populateOrganizationModel(model,
					organizationIdValue,
					organizationNameValue,
					"No organizations matched " + criteriaDescription + ".",
					null,
					null);
			} else {
				String message = "Retrieved %d organization(s) matching %s."
					.formatted(organizations.size(), criteriaDescription);
				populateOrganizationModel(model,
					organizationIdValue,
					organizationNameValue,
					message,
					null,
					toPrettyJson(organizations));
			}
		} catch (IllegalArgumentException | IllegalStateException ex) {
			populateOrganizationModel(model,
				organizationIdValue,
				organizationNameValue,
				null,
				ex.getMessage(),
				null);
		} catch (RestClientResponseException ex) {
			String message = "Organization lookup failed (%d): %s"
				.formatted(ex.getRawStatusCode(), ex.getResponseBodyAsString());
			populateOrganizationModel(model,
				organizationIdValue,
				organizationNameValue,
				null,
				message,
				null);
		} catch (RestClientException ex) {
			populateOrganizationModel(model,
				organizationIdValue,
				organizationNameValue,
				null,
				"Unable to reach Hanaeco server: " + ex.getMessage(),
				null);
		}

		return "organization";
	}

	@PostMapping("/products/query")
	public String queryProducts(@RequestParam(required = false) String organizationUid,
								@RequestParam(required = false) String productIdentifier,
								@RequestParam(required = false) String productId,
								@RequestParam(required = false) String cnCodeId,
								Model model) {
		String organizationUidValue = organizationUid != null ? organizationUid.trim() : "";
		String productIdentifierValue = productIdentifier != null ? productIdentifier.trim() : "";
		String productIdValue = productId != null ? productId.trim() : "";
		String cnCodeIdValue = cnCodeId != null ? cnCodeId.trim() : "";

		if (!StringUtils.hasText(organizationUidValue)) {
			populateProductModel(model,
				organizationUidValue,
				productIdentifierValue,
				productIdValue,
				cnCodeIdValue,
				null,
				"Provide an organization UID before querying products.",
				null);
			return "product";
		}

		try {
			List<ProductDto> products = productLookupService.fetchProducts(
				organizationUidValue,
				productIdentifierValue,
				productIdValue,
				cnCodeIdValue);
			String criteriaDescription = buildProductCriteriaDescription(organizationUidValue, productIdentifierValue, productIdValue, cnCodeIdValue);
			if (products.isEmpty()) {
				populateProductModel(model,
					organizationUidValue,
					productIdentifierValue,
					productIdValue,
					cnCodeIdValue,
					"No products matched " + criteriaDescription + ".",
					null,
					null);
			} else {
				String message = "Retrieved %d product(s) matching %s."
					.formatted(products.size(), criteriaDescription);
				populateProductModel(model,
					organizationUidValue,
					productIdentifierValue,
					productIdValue,
					cnCodeIdValue,
					message,
					null,
					toPrettyJson(products));
			}
		} catch (IllegalArgumentException | IllegalStateException ex) {
			populateProductModel(model,
				organizationUidValue,
				productIdentifierValue,
				productIdValue,
				cnCodeIdValue,
				null,
				ex.getMessage(),
				null);
		} catch (RestClientResponseException ex) {
			String message = "Product lookup failed (%d): %s"
				.formatted(ex.getRawStatusCode(), ex.getResponseBodyAsString());
			populateProductModel(model,
				organizationUidValue,
				productIdentifierValue,
				productIdValue,
				cnCodeIdValue,
				null,
				message,
				null);
		} catch (RestClientException ex) {
			populateProductModel(model,
				organizationUidValue,
				productIdentifierValue,
				productIdValue,
				cnCodeIdValue,
				null,
				"Unable to reach Hanaeco server: " + ex.getMessage(),
				null);
		}

		return "product";
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

		populateCommonAttributes(model);

		model.addAttribute("configurationMessage", configurationMessage);
		model.addAttribute("configurationError", configurationError);
		model.addAttribute("fetchResult", fetchResult);
		model.addAttribute("fetchError", fetchError);
	}

	private void populateOrganizationModel(Model model,
										   String organizationIdFieldValue,
										   String organizationNameFieldValue,
										   String lookupMessage,
										   String lookupError,
										   String lookupResult) {
		populateCommonAttributes(model);
		model.addAttribute("organizationIdFieldValue", organizationIdFieldValue);
		model.addAttribute("organizationNameFieldValue", organizationNameFieldValue);
		model.addAttribute("organizationLookupMessage", lookupMessage);
		model.addAttribute("organizationLookupError", lookupError);
		model.addAttribute("organizationLookupResult", lookupResult);
	}

	private void populateProductModel(Model model,
									  String organizationUidFieldValue,
									  String productIdentifierFieldValue,
									  String productIdFieldValue,
									  String cnCodeIdFieldValue,
									  String lookupMessage,
									  String lookupError,
									  String lookupResult) {
		populateCommonAttributes(model);
		model.addAttribute("productOrganizationUidFieldValue", organizationUidFieldValue);
		model.addAttribute("productIdentifierFieldValue", productIdentifierFieldValue);
		model.addAttribute("productIdFieldValue", productIdFieldValue);
		model.addAttribute("productCnCodeIdFieldValue", cnCodeIdFieldValue);
		model.addAttribute("productLookupMessage", lookupMessage);
		model.addAttribute("productLookupError", lookupError);
		model.addAttribute("productLookupResult", lookupResult);
	}

	private void populateCommonAttributes(Model model) {
		Optional<HanaecoClientDetails> current = clientContext.current();
		String defaultBaseUrl = propertyProvider.getHanaecoBaseUrl();
		String configuredBaseUrl = current.map(HanaecoClientDetails::baseUrl).orElse(defaultBaseUrl);

		model.addAttribute("baseUrlFieldValue", configuredBaseUrl);
		model.addAttribute("configuredBaseUrl", configuredBaseUrl);
		model.addAttribute("apiKeyFieldValue", "");
		model.addAttribute("configurationReady", current.isPresent());
	}

	private String toPrettyJson(Object dto) {
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
		} catch (JsonProcessingException ex) {
			return dto.toString();
		}
	}

	private String buildProductCriteriaDescription(String organizationUidValue,
												   String productIdentifierValue,
												   String productIdValue,
												   String cnCodeIdValue) {
		List<String> parts = new ArrayList<>();
		parts.add("organization UID '" + organizationUidValue + "'");
		if (StringUtils.hasText(productIdentifierValue)) {
			parts.add("identifier '" + productIdentifierValue + "'");
		}
		if (StringUtils.hasText(productIdValue)) {
			parts.add("ID '" + productIdValue + "'");
		}
		if (StringUtils.hasText(cnCodeIdValue)) {
			parts.add("CN code '" + cnCodeIdValue + "'");
		}
		return String.join(", ", parts);
	}

	private String buildOrganizationCriteriaDescription(String organizationIdValue, String organizationNameValue) {
		if (StringUtils.hasText(organizationIdValue)) {
			return "an ID containing '" + organizationIdValue + "'";
		}
		if (StringUtils.hasText(organizationNameValue)) {
			return "a name containing '" + organizationNameValue + "'";
		}
		return "the supplied criteria";
	}
}
