package se.sundsvall.archive.integration.formpipeproxy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;
import se.sundsvall.dept44.problem.ThrowableProblem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ExtendWith(MockitoExtension.class)
class FormpipeProxyIntegrationTest {

	@Mock
	private FormpipeProxyClient mockClient;

	@InjectMocks
	private FormpipeProxyIntegration formpipeProxyIntegration;

	@Test
	void doImport400BadRequest() {

		final var request = new ImportRequest();

		when(mockClient.postImport(any(ImportRequest.class)))
			.thenReturn(ResponseEntity.badRequest().build());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> formpipeProxyIntegration.doImport(request))
			.satisfies(thrownProblem -> {
				assertThat(thrownProblem.getMessage()).isEqualTo("Client error");
				assertThat(thrownProblem.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);
				assertThat(thrownProblem.getCauseAsProblem()).satisfies(cause -> assertThat(cause.getStatus()).isEqualTo(BAD_GATEWAY));
			});
	}

	@Test
	void doImport500InternalServerError() {

		final var request = new ImportRequest();

		when(mockClient.postImport(any(ImportRequest.class)))
			.thenReturn(ResponseEntity.internalServerError().build());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> formpipeProxyIntegration.doImport(request))
			.satisfies(thrownProblem -> {
				assertThat(thrownProblem.getMessage()).isEqualTo("Server error");
				assertThat(thrownProblem.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);
			});
	}

	@Test
	void doImportUnknownError() {

		final var request = new ImportRequest();

		when(mockClient.postImport(any(ImportRequest.class)))
			.thenReturn(ResponseEntity.status(HttpStatus.FOUND).build());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> formpipeProxyIntegration.doImport(request))
			.satisfies(thrownProblem -> {
				assertThat(thrownProblem.getMessage()).isEqualTo("Unexpected error");
				assertThat(thrownProblem.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);
			});
	}

	@Test
	void doImport200Ok() {
		when(mockClient.postImport(any(ImportRequest.class)))
			.thenReturn(ResponseEntity.ok(new ImportResponse()));

		final var response = formpipeProxyIntegration.doImport(new ImportRequest());

		assertThat(response).isNotNull();
	}
}
