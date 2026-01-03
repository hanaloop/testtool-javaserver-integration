package com.hanaloop.tool.hanaecointegration.client;

import com.hanaloop.hanaeco.api.OrgResourcesApi;
import com.hanaloop.hanaeco.invoker.ApiClient;
import com.hanaloop.hanaeco.model.ProductControllerGetProductsPaged200Response;
import com.hanaloop.hanaeco.model.ProductDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductLookupService {

	private static final String API_KEY_HEADER = "X-API-KEY";

	private final HanaecoClientContext clientContext;

	public ProductLookupService(HanaecoClientContext clientContext) {
		this.clientContext = clientContext;
	}

	public List<ProductDto> fetchProducts(String organizationUid,
										  String identifier,
										  String productId,
										  String cnCodeId) {
		String orgUid = StringUtils.hasText(organizationUid) ? organizationUid.trim() : "";
		if (!StringUtils.hasText(orgUid)) {
			throw new IllegalArgumentException("Organization UID is required.");
		}

		HanaecoClientDetails details = requireConfiguredClient();
		Map<String, String> filters = buildFilters(identifier, productId, cnCodeId);

		ProductControllerGetProductsPaged200Response response = createApi(details)
			.productControllerGetProductsPaged(orgUid, null, null, null, null, filters.isEmpty() ? null : filters);

		List<ProductDto> products = response != null ? response.getContent() : Collections.emptyList();
		return CollectionUtils.isEmpty(products) ? Collections.emptyList() : products;
	}

	private Map<String, String> buildFilters(String identifier, String productId, String cnCodeId) {
		Map<String, String> filters = new LinkedHashMap<>();
		if (StringUtils.hasText(identifier)) {
			filters.put("identifier%3Acontains", identifier.trim());
		}
		if (StringUtils.hasText(productId)) {
			filters.put("id%3Acontains", productId.trim());
		}
		if (StringUtils.hasText(cnCodeId)) {
			filters.put("cnCode.id%3Acontains", cnCodeId.trim());
		}
		return filters;
	}

	private HanaecoClientDetails requireConfiguredClient() {
		return clientContext.current()
			.orElseThrow(() -> new IllegalStateException("Configure the Hanaeco client before querying products."));
	}

	private OrgResourcesApi createApi(HanaecoClientDetails details) {
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(details.baseUrl());
		apiClient.addDefaultHeader(API_KEY_HEADER, details.apiKey());
		return new OrgResourcesApi(apiClient);
	}
}
