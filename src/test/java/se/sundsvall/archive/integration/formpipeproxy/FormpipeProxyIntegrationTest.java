package se.sundsvall.archive.integration.formpipeproxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static se.sundsvall.archive.integration.formpipeproxy.FormpipeProxyIntegration.INTEGRATION_NAME;
import static se.sundsvall.archive.integration.formpipeproxy.FormpipeProxyIntegration.INTEGRATION_NAME_PROPERTY;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

@ActiveProfiles("junit")
@ExtendWith(MockitoExtension.class)
class FormpipeProxyIntegrationTest {

	@Mock
	private FormpipeProxyClient mockClient;

	private FormpipeProxyIntegration formpipeProxyIntegration;

	@BeforeEach
	void setUp() {
		formpipeProxyIntegration = new FormpipeProxyIntegration(mockClient);
	}

	@Test
	void test_doImport_400_BAD_REQUEST() {

		final var request = new ImportRequest();

		when(mockClient.postImport(any(ImportRequest.class)))
			.thenReturn(ResponseEntity.badRequest().build());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> formpipeProxyIntegration.doImport(request))
			.satisfies(thrownProblem -> {
				assertThat(thrownProblem.getMessage()).isEqualTo("Client error");
				assertThat(thrownProblem.getParameters()).containsEntry(INTEGRATION_NAME_PROPERTY, INTEGRATION_NAME);
				assertThat(thrownProblem.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
				assertThat(thrownProblem.getCause()).satisfies(cause -> {
					assertThat(cause.getStatus()).isEqualTo(Status.BAD_GATEWAY);
				});
			});
	}

	@Test
	void test_doImport_500_INTERNAL_SERVER_ERROR() {

		final var request = new ImportRequest();

		when(mockClient.postImport(any(ImportRequest.class)))
			.thenReturn(ResponseEntity.internalServerError().build());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> formpipeProxyIntegration.doImport(request))
			.satisfies(thrownProblem -> {
				assertThat(thrownProblem.getMessage()).isEqualTo("Server error");
				assertThat(thrownProblem.getParameters()).containsEntry(INTEGRATION_NAME_PROPERTY, INTEGRATION_NAME);
				assertThat(thrownProblem.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
			});
	}

	@Test
	void test_doImport_unknown_error() {

		final var request = new ImportRequest();

		when(mockClient.postImport(any(ImportRequest.class)))
			.thenReturn(ResponseEntity.status(HttpStatus.FOUND).build());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> formpipeProxyIntegration.doImport(request))
			.satisfies(thrownProblem -> {
				assertThat(thrownProblem.getMessage()).isEqualTo("Unexpected error");
				assertThat(thrownProblem.getParameters()).containsEntry(INTEGRATION_NAME_PROPERTY, INTEGRATION_NAME);
				assertThat(thrownProblem.getStatus()).isEqualTo(Status.INTERNAL_SERVER_ERROR);
			});
	}

	@Test
    void test_doImport_200_OK() {
        when(mockClient.postImport(any(ImportRequest.class)))
            .thenReturn(ResponseEntity.ok(new ImportResponse()));

        final var response = formpipeProxyIntegration.doImport(new ImportRequest());

        assertThat(response).isNotNull();
    }
}
