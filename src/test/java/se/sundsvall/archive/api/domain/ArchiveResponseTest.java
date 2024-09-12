package se.sundsvall.archive.api.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ArchiveResponseTest {

	@Test
	void gettersAndSetters() {
		final var errorDetails = new ArchiveResponse.ErrorDetails();
		errorDetails.setErrorCode(777);
		errorDetails.setErrorMessage("someErrorMessage");
		errorDetails.setServiceName("someServiceName");

		final var response = new ArchiveResponse();
		response.setArchiveId("someArchiveId");
		response.setErrorDetails(errorDetails);

		assertThat(response.getArchiveId()).isEqualTo("someArchiveId");
		assertThat(response.getErrorDetails()).satisfies(e -> {
			assertThat(e.getErrorCode()).isEqualTo(777);
			assertThat(e.getErrorMessage()).isEqualTo("someErrorMessage");
			assertThat(e.getServiceName()).isEqualTo("someServiceName");
		});
	}

	@Test
	void builder() {
		final var response = ArchiveResponse.builder()
			.withArchiveId("someArchiveId")
			.withErrorDetails(ArchiveResponse.ErrorDetails.builder()
				.withErrorCode(888)
				.withErrorMessage("someErrorMessage")
				.withServiceName("someServiceName")
				.build())
			.build();

		assertThat(response.getArchiveId()).isEqualTo("someArchiveId");
		assertThat(response.getErrorDetails()).satisfies(e -> {
			assertThat(e.getErrorCode()).isEqualTo(888);
			assertThat(e.getErrorMessage()).isEqualTo("someErrorMessage");
			assertThat(e.getServiceName()).isEqualTo("someServiceName");
		});
	}
}
