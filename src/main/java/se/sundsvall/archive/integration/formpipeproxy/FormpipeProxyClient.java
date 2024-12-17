package se.sundsvall.archive.integration.formpipeproxy;

import static se.sundsvall.archive.integration.formpipeproxy.FormpipeProxyIntegration.INTEGRATION_NAME;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

@FeignClient(
	name = INTEGRATION_NAME,
	url = "${integration.formpipe-proxy.base-url}",
	configuration = FormpipeProxyIntegrationConfiguration.class)
@CircuitBreaker(name = INTEGRATION_NAME)
interface FormpipeProxyClient {

	@PostMapping("/api/import")
	ResponseEntity<ImportResponse> postImport(ImportRequest request);
}
