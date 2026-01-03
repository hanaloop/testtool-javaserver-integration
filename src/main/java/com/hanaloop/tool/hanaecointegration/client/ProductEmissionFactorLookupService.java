package com.hanaloop.tool.hanaecointegration.client;

import com.hanaloop.hanaeco.api.OrgResourcesApi;
import com.hanaloop.hanaeco.invoker.ApiClient;
import com.hanaloop.hanaeco.model.EmissionFactorDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
public class ProductEmissionFactorLookupService {

	private static final String API_KEY_HEADER = "X-API-KEY";

	private final HanaecoClientContext clientContext;

	public ProductEmissionFactorLookupService(HanaecoClientContext clientContext) {
		this.clientContext = clientContext;
	}

	public List<EmissionFactorDto> fetchEmissionFactors(String productUid, String year) {
		String productUidValue = StringUtils.hasText(productUid) ? productUid.trim() : "";
		if (!StringUtils.hasText(productUidValue)) {
			throw new IllegalArgumentException("Product UID is required.");
		}

		HanaecoClientDetails details = requireConfiguredClient();
		BigDecimal yearParam = parseYear(year);

		List<EmissionFactorDto> emissionFactors = createApi(details)
			.productControllerGetProductEmissionFactors(productUidValue, yearParam);

		return CollectionUtils.isEmpty(emissionFactors) ? Collections.emptyList() : emissionFactors;
	}

	private BigDecimal parseYear(String year) {
		if (!StringUtils.hasText(year)) {
			return null;
		}
		String trimmed = year.trim();
		try {
			return new BigDecimal(trimmed);
		} catch (NumberFormatException ex) {
			throw new IllegalArgumentException("Year must be a number.");
		}
	}

	private HanaecoClientDetails requireConfiguredClient() {
		return clientContext.current()
			.orElseThrow(() -> new IllegalStateException("Configure the Hanaeco client before querying emission factors."));
	}

	private OrgResourcesApi createApi(HanaecoClientDetails details) {
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(details.baseUrl());
		apiClient.addDefaultHeader(API_KEY_HEADER, details.apiKey());
		return new OrgResourcesApi(apiClient);
	}
}
