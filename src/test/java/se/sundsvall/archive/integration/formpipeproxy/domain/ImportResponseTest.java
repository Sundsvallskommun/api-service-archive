package se.sundsvall.archive.integration.formpipeproxy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ImportResponseTest {

	@Test
	void gettersAndSetters() {
		final var errorDetails = new ImportResponse.ErrorDetails();
		errorDetails.setErrorCode(555);
		errorDetails.setErrorMessage("someErrorMessage");
		errorDetails.setServiceName("someServiceName");

		final var response = new ImportResponse();
		response.setImportedFileSetId("someImportedFileSetId");
		response.setErrorDetails(errorDetails);

		assertThat(response.getImportedFileSetId()).isEqualTo("someImportedFileSetId");
		assertThat(response.getErrorDetails()).satisfies(e -> {
			assertThat(e.getErrorCode()).isEqualTo(555);
			assertThat(e.getErrorMessage()).isEqualTo("someErrorMessage");
			assertThat(e.getServiceName()).isEqualTo("someServiceName");
		});
	}
}
