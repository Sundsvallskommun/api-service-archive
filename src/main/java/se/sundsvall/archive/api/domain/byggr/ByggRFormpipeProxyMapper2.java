package se.sundsvall.archive.api.domain.byggr;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import se.sundsvall.archive.api.domain.ArchiveResponse;
import se.sundsvall.archive.integration.formpipeproxy.FormpipeProxyMapper;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

@Component
public class ByggRFormpipeProxyMapper2 implements FormpipeProxyMapper<ByggRArchiveRequest2, ArchiveResponse> {

    private final String submissionAgreementId;

    public ByggRFormpipeProxyMapper2(
            @Value("${byggr.submission-agreement-id}") final String submissionAgreementId) {
        this.submissionAgreementId = submissionAgreementId;
    }

    @Override
    public ImportRequest map(final ByggRArchiveRequest2 byggRArchiveRequest) {
        return ImportRequest.builder()
            .withSubmissionAgreementId(submissionAgreementId)
            .withUuid(UUID.randomUUID().toString())
            .withMetadataXml(byggRArchiveRequest.getMetadata())
            //.withPersonalDataFlag(false)
            .withConfidentialityLevel(0)
            //.withConfidentialityDegradationDate(LocalDateTime.now().plusYears(1))
            .withPreservationObject(ImportRequest.PreservationObject.builder()
                .withFileExtension(byggRArchiveRequest.getAttachment().getExtension())
                .withFileName(byggRArchiveRequest.getAttachment().getName())
                .withData(byggRArchiveRequest.getAttachment().getFile())
                .build())
            .build();
    }

    @Override
    public ArchiveResponse map(final ImportResponse response) {
        return ArchiveResponse.builder()
            .withArchiveId(response.getImportedFileSetId())
            .withErrorDetails(Optional.ofNullable(response.getErrorDetails())
                .map(errorDetails -> ArchiveResponse.ErrorDetails.builder()
                    .withErrorCode(errorDetails.getErrorCode())
                    .withErrorMessage(errorDetails.getErrorMessage())
                    .withServiceName(errorDetails.getServiceName())
                    .build())
                .orElse(null))
            .build();
    }
}
