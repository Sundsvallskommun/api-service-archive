package se.sundsvall.archive.api.domain.byggr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.ThrowableProblem;

import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

@ExtendWith(MockitoExtension.class)
class ByggRFormpipeProxyMapperTest {

	private static final String SUBMISSION_AGREEMENT_ID = "someSubmissionAgreementId";

	@Mock
	private MetadataUtil mockMetadataUtil;

	private ByggRFormpipeProxyMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new ByggRFormpipeProxyMapper(SUBMISSION_AGREEMENT_ID, mockMetadataUtil);
	}

	@Test
	void mapRequest() {
		when(mockMetadataUtil.isValidMetadata(any(String.class))).thenReturn(true);
		when(mockMetadataUtil.getConfidentialityLevel(any(String.class))).thenReturn(1);
		when(mockMetadataUtil.replaceAttachmentNameAndLink(any(String.class), any(String.class), any(String.class)))
			.thenReturn("someMetadata");

		final var attachment = new Attachment();
		attachment.setName("someName");
		attachment.setExtension("someExtension");
		attachment.setFile("someFile");

		final var archiveRequest = new ByggRArchiveRequest();
		archiveRequest.setMetadata("someMetadata");
		archiveRequest.setAttachment(attachment);

		final var importRequest = mapper.map(archiveRequest);
		assertThat(importRequest.getSubmissionAgreementId()).isEqualTo(SUBMISSION_AGREEMENT_ID);
		assertThat(importRequest.getUuid()).isNotBlank();
		assertThat(importRequest.getMetadataXml()).isEqualTo(mapper.toBase64("someMetadata"));
		assertThat(importRequest.getConfidentialityLevel()).isOne();
		assertThat(importRequest.getPreservationObject()).satisfies(preservationObject -> {
			assertThat(preservationObject.getFileName()).isNotBlank();
			assertThat(preservationObject.getFileExtension()).isEqualTo("someExtension");
			assertThat(preservationObject.getData()).isEqualTo("someFile");
		});

		verify(mockMetadataUtil, times(1)).isValidMetadata(any(String.class));
		verify(mockMetadataUtil, times(1)).getConfidentialityLevel(any(String.class));
	}

	@Test
	void mapRequestWithInvalidMetadata() {
		when(mockMetadataUtil.isValidMetadata(any(String.class))).thenReturn(false);

		final var attachment = new Attachment();
		attachment.setName("someName");
		attachment.setExtension("someExtension");
		attachment.setFile("someFile");

		final var archiveRequest = new ByggRArchiveRequest();
		archiveRequest.setMetadata("someMetadata");
		archiveRequest.setAttachment(attachment);

		assertThatExceptionOfType(ThrowableProblem.class).isThrownBy(() -> mapper.map(archiveRequest));
	}

	@Test
	void mapResponse() {
		final var errorDetails = new ImportResponse.ErrorDetails();
		errorDetails.setErrorCode(999);
		errorDetails.setErrorMessage("someErrorMessage");
		errorDetails.setServiceName("someServiceName");

		final var importResponse = new ImportResponse();
		importResponse.setImportedFileSetId("someImportedFileSetId");
		importResponse.setErrorDetails(errorDetails);

		final var archiveResponse = mapper.map(importResponse);
		assertThat(archiveResponse.getArchiveId()).isEqualTo("someImportedFileSetId");
		assertThat(archiveResponse.getErrorDetails()).satisfies(e -> {
			assertThat(e.getErrorCode()).isEqualTo(999);
			assertThat(e.getErrorMessage()).isEqualTo("someErrorMessage");
			assertThat(e.getServiceName()).isEqualTo("someServiceName");
		});
	}
}
