package se.sundsvall.archive.api.domain.byggr;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import se.sundsvall.archive.api.domain.ArchiveResponse;
import se.sundsvall.archive.integration.formpipeproxy.FormpipeProxyMapper;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

@Component
public class ByggRFormpipeProxyMapper implements FormpipeProxyMapper<ByggRArchiveRequest, ArchiveResponse> {

    static final String SUBMISSION_AGREEMENT_ID = "AGS";

    @Override
    public ImportRequest map(final ByggRArchiveRequest byggRArchiveRequest) {
        return ImportRequest.builder()
            .withSubmissionAgreementId(SUBMISSION_AGREEMENT_ID)
            .withUuid(UUID.randomUUID().toString())
            .withMetadataXml(toBase64(byggRArchiveRequest.getMetadata()))
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
