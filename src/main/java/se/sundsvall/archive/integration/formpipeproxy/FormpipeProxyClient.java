package se.sundsvall.archive.integration.formpipeproxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

@FeignClient(
	name = FormpipeProxyIntegration.INTEGRATION_NAME,
	url = "${integration.formpipe-proxy.base-url}",
	configuration = FormpipeProxyIntegrationConfiguration.class)
interface FormpipeProxyClient {

	@PostMapping("/api/import")
	ResponseEntity<ImportResponse> postImport(ImportRequest request);
}
