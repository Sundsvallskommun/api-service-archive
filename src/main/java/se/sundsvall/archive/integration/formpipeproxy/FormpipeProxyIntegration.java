package se.sundsvall.archive.integration.formpipeproxy;

import java.util.Optional;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;
import se.sundsvall.dept44.problem.Problem;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
@EnableConfigurationProperties(FormpipeProxyIntegrationProperties.class)
public class FormpipeProxyIntegration {

	static final String INTEGRATION_NAME = "FormpipeProxy";

	private final FormpipeProxyClient client;

	FormpipeProxyIntegration(final FormpipeProxyClient client) {
		this.client = client;
	}

	public ImportResponse doImport(final ImportRequest request) {
		final var resp = client.postImport(request);

		final var httpStatus = Optional.ofNullable(HttpStatus.resolve(resp.getStatusCode().value()))
			.orElseThrow(() -> Problem.valueOf(INTERNAL_SERVER_ERROR, "Could not resolve HTTP-status from FormpipeProxy response!"));

		return switch (httpStatus.series()) {
			case SUCCESSFUL -> resp.getBody();
			case CLIENT_ERROR -> throw Problem.builder()
				.withTitle("Client error")
				.withStatus(INTERNAL_SERVER_ERROR)
				.withCause(Problem.builder()
					.withStatus(BAD_GATEWAY)
					.build())
				.build();
			case SERVER_ERROR -> throw Problem.builder()
				.withTitle("Server error")
				.withStatus(INTERNAL_SERVER_ERROR)
				.build();
			default -> throw Problem.builder()
				.withTitle("Unexpected error")
				.withStatus(INTERNAL_SERVER_ERROR)
				.build();
		};
	}
}
