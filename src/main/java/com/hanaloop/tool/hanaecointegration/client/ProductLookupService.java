package com.hanaloop.tool.hanaecointegration.client;

import com.hanaloop.hanaeco.api.OrganizationApi;
import com.hanaloop.hanaeco.invoker.ApiClient;
import com.hanaloop.hanaeco.model.ProductDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

@Component
public class ProductLookupService {

	private static final String API_KEY_HEADER = "X-API-KEY";

	private final HanaecoClientContext clientContext;

	public ProductLookupService(HanaecoClientContext clientContext) {
		this.clientContext = clientContext;
	}

	public List<ProductDto> fetchProducts(String organizationUid) {
		if (!StringUtils.hasText(organizationUid)) {
			return Collections.emptyList();
		}
		HanaecoClientDetails details = requireConfiguredClient();
		List<ProductDto> products = createApi(details)
			.productControllerGetProducts(organizationUid.trim());
		return CollectionUtils.isEmpty(products) ? Collections.emptyList() : products;
	}

	private HanaecoClientDetails requireConfiguredClient() {
		return clientContext.current()
			.orElseThrow(() -> new IllegalStateException("Configure the Hanaeco client before querying products."));
	}

	private OrganizationApi createApi(HanaecoClientDetails details) {
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(details.baseUrl());
		apiClient.addDefaultHeader(API_KEY_HEADER, details.apiKey());
		return new OrganizationApi(apiClient);
	}
}
