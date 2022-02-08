package se.sundsvall.archive.integration.formpipeproxy;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

@Component
@EnableConfigurationProperties(FormpipeProxyIntegrationProperties.class)
public class FormpipeProxyIntegration {

    static final String INTEGRATION_NAME = "FormpipeProxy";

    private final RestTemplate restTemplate;

    public FormpipeProxyIntegration(
            @Qualifier("integration.formpipe-proxy.resttemplate") final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ImportResponse doImport(final ImportRequest request) {
        var response = restTemplate.exchange("/api/import",
            HttpMethod.POST, new HttpEntity<>(request, createHeaders()), ImportResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else if (response.getStatusCode().is4xxClientError()) {
            throw Problem.builder()
                .withTitle("Client error")
                .with("integrationName", INTEGRATION_NAME)
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .withCause(Problem.builder()
                    .withStatus(Status.BAD_GATEWAY)
                    .build())
                .build();
        } else if (response.getStatusCode().is5xxServerError()) {
            throw Problem.builder()
                .withTitle("Server error")
                .with("integrationName", INTEGRATION_NAME)
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .build();
        }

        throw Problem.builder()
            .withTitle("Unexpected error")
            .with("integrationName", INTEGRATION_NAME)
            .withStatus(Status.INTERNAL_SERVER_ERROR)
            .build();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
