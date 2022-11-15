package se.sundsvall.archive.integration.formpipeproxy;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

@Component
@EnableConfigurationProperties(FormpipeProxyIntegrationProperties.class)
public class FormpipeProxyIntegration {

    static final String INTEGRATION_NAME = "FormpipeProxy";

    static final String CIRCUIT_BREAKER_NAME = "formpipe-proxy";

    private final FormpipeProxyClient client;

    public FormpipeProxyIntegration(final FormpipeProxyClient client) {
        this.client = client;
    }

    public ImportResponse doImport(final ImportRequest request) {
        var resp = client.postImport(request);

        return switch (resp.getStatusCode().series()) {
            case SUCCESSFUL -> resp.getBody();
            case CLIENT_ERROR -> throw Problem.builder()
                .withTitle("Client error")
                .with("integrationName", INTEGRATION_NAME)
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .withCause(Problem.builder()
                    .withStatus(Status.BAD_GATEWAY)
                    .build())
                .build();
            case SERVER_ERROR -> throw Problem.builder()
                .withTitle("Server error")
                .with("integrationName", INTEGRATION_NAME)
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .build();
            default -> throw Problem.builder()
                .withTitle("Unexpected error")
                .with("integrationName", INTEGRATION_NAME)
                .withStatus(Status.INTERNAL_SERVER_ERROR)
                .build();
        };
    }
}
