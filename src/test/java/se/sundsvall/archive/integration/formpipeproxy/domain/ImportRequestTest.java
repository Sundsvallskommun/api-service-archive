package se.sundsvall.archive.integration.formpipeproxy.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ImportRequestTest {

	private static final LocalDateTime CONFIDENTIALITY_DEGRADATION_DATE = LocalDateTime.now().plusYears(1);

	@Test
	void gettersAndSetters() {
		final var preservationObject = new ImportRequest.PreservationObject();
		preservationObject.setFileName("someFileName");
		preservationObject.setFileExtension("someFileExtension");
		preservationObject.setData("someData");

		final var request = new ImportRequest();
		request.setSubmissionAgreementId("someSubmissionAgreementId");
		request.setUuid("someUuid");
		request.setConfidentialityLevel(3);
		request.setConfidentialityDegradationDate(CONFIDENTIALITY_DEGRADATION_DATE);
		request.setPersonalDataFlag(true);
		request.setMetadataXml("someMetadataXml");
		request.setPreservationObject(preservationObject);

		assertThat(request.getSubmissionAgreementId()).isEqualTo("someSubmissionAgreementId");
		assertThat(request.getUuid()).isEqualTo("someUuid");
		assertThat(request.getConfidentialityLevel()).isEqualTo(3);
		assertThat(request.getConfidentialityDegradationDate()).isEqualTo(CONFIDENTIALITY_DEGRADATION_DATE);
		assertThat(request.getPersonalDataFlag()).isTrue();
		assertThat(request.getMetadataXml()).isEqualTo("someMetadataXml");
		assertThat(request.getPreservationObject()).satisfies(p -> {
			assertThat(p.getFileName()).isEqualTo("someFileName");
			assertThat(p.getFileExtension()).isEqualTo("someFileExtension");
			assertThat(p.getData()).isEqualTo("someData");
		});
	}

	@Test
	void builders() {
		final var request = ImportRequest.builder()
			.withSubmissionAgreementId("someSubmissionAgreementId")
			.withUuid("someUuid")
			.withConfidentialityLevel(3)
			.withConfidentialityDegradationDate(CONFIDENTIALITY_DEGRADATION_DATE)
			.withPersonalDataFlag(true)
			.withMetadataXml("someMetadataXml")
			.withPreservationObject(ImportRequest.PreservationObject.builder()
				.withFileName("someFileName")
				.withFileExtension("someFileExtension")
				.withData("someData")
				.build())
			.build();

		assertThat(request.getSubmissionAgreementId()).isEqualTo("someSubmissionAgreementId");
		assertThat(request.getUuid()).isEqualTo("someUuid");
		assertThat(request.getConfidentialityLevel()).isEqualTo(3);
		assertThat(request.getConfidentialityDegradationDate()).isEqualTo(CONFIDENTIALITY_DEGRADATION_DATE);
		assertThat(request.getPersonalDataFlag()).isTrue();
		assertThat(request.getMetadataXml()).isEqualTo("someMetadataXml");
		assertThat(request.getPreservationObject()).satisfies(p -> {
			assertThat(p.getFileName()).isEqualTo("someFileName");
			assertThat(p.getFileExtension()).isEqualTo("someFileExtension");
			assertThat(p.getData()).isEqualTo("someData");
		});
	}
}
