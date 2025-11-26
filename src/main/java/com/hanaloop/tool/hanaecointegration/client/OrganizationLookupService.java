package com.hanaloop.tool.hanaecointegration.client;

import com.hanaloop.hanaeco.api.OrganizationApi;
import com.hanaloop.hanaeco.invoker.ApiClient;
import com.hanaloop.hanaeco.model.OrganizationDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class OrganizationLookupService {

	private static final String API_KEY_HEADER = "X-API-KEY";

	private final HanaecoClientContext clientContext;

	public OrganizationLookupService(HanaecoClientContext clientContext) {
		this.clientContext = clientContext;
	}

	public List<OrganizationDto> fetchOrganizations(String organizationId, String organizationName) {
		Map<String, String> filters = buildFilters(organizationId, organizationName);
		if (filters.isEmpty()) {
			throw new IllegalArgumentException("Provide either an organization ID or name.");
		}

		HanaecoClientDetails details = requireConfiguredClient();
		List<OrganizationDto> organizations = createApi(details)
			.organizationControllerGetOrganizations(filters);
		return CollectionUtils.isEmpty(organizations) ? Collections.emptyList() : organizations;
	}

	private Map<String, String> buildFilters(String organizationId, String organizationName) {
		if (StringUtils.hasText(organizationId)) {
			return Collections.singletonMap("id%3Acontains", organizationId.trim());
		}
		if (StringUtils.hasText(organizationName)) {
			return Collections.singletonMap("name%3Acontains", organizationName.trim());
		}
		return Collections.emptyMap();
	}

	private HanaecoClientDetails requireConfiguredClient() {
		return clientContext.current()
			.orElseThrow(() -> new IllegalStateException("Configure the Hanaeco client before querying organizations."));
	}

	private OrganizationApi createApi(HanaecoClientDetails details) {
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(details.baseUrl());
		apiClient.addDefaultHeader(API_KEY_HEADER, details.apiKey());
		return new OrganizationApi(apiClient);
	}
}
