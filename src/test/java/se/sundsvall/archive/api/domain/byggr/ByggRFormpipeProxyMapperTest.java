package se.sundsvall.archive.api.domain.byggr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
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
    private MetadataValidator mockMetadataValidator;

    private ByggRFormpipeProxyMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ByggRFormpipeProxyMapper(SUBMISSION_AGREEMENT_ID, mockMetadataValidator);
    }

    @Test
    void test_mapRequest() {
        when(mockMetadataValidator.isValidMetadata(any(String.class))).thenReturn(true);

        var attachment = new Attachment();
        attachment.setName("someName");
        attachment.setExtension("someExtension");
        attachment.setFile("someFile");

        var archiveRequest = new ByggRArchiveRequest();
        archiveRequest.setMetadata("someMetadata");
        archiveRequest.setAttachment(attachment);

        var importRequest = mapper.map(archiveRequest);
        assertThat(importRequest.getSubmissionAgreementId()).isEqualTo(SUBMISSION_AGREEMENT_ID);
        assertThat(importRequest.getUuid()).isNotBlank();
        assertThat(importRequest.getMetadataXml()).isEqualTo(mapper.toBase64("someMetadata"));
        assertThat(importRequest.getConfidentialityLevel()).isEqualTo(0);
        assertThat(importRequest.getPreservationObject()).satisfies(preservationObject -> {
            assertThat(preservationObject.getFileName()).isEqualTo("someName");
            assertThat(preservationObject.getFileExtension()).isEqualTo("someExtension");
            assertThat(preservationObject.getData()).isEqualTo("someFile");
        });
    }

    @Test
    void test_mapRequest_withInvalidMetadata() {
        when(mockMetadataValidator.isValidMetadata(any(String.class))).thenReturn(false);

        var attachment = new Attachment();
        attachment.setName("someName");
        attachment.setExtension("someExtension");
        attachment.setFile("someFile");

        var archiveRequest = new ByggRArchiveRequest();
        archiveRequest.setMetadata("someMetadata");
        archiveRequest.setAttachment(attachment);

        assertThatExceptionOfType(ThrowableProblem.class).isThrownBy(() -> mapper.map(archiveRequest));
    }

    @Test
    void test_mapResponse() {
        var errorDetails = new ImportResponse.ErrorDetails();
        errorDetails.setErrorCode(999);
        errorDetails.setErrorMessage("someErrorMessage");
        errorDetails.setServiceName("someServiceName");

        var importResponse = new ImportResponse();
        importResponse.setImportedFileSetId("someImportedFileSetId");
        importResponse.setErrorDetails(errorDetails);

        var archiveResponse = mapper.map(importResponse);
        assertThat(archiveResponse.getArchiveId()).isEqualTo("someImportedFileSetId");
        assertThat(archiveResponse.getErrorDetails()).satisfies(e -> {
            assertThat(e.getErrorCode()).isEqualTo(999);
            assertThat(e.getErrorMessage()).isEqualTo("someErrorMessage");
            assertThat(e.getServiceName()).isEqualTo("someServiceName");
        });
    }
}
