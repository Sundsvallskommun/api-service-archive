package se.sundsvall.archive.api.domain.byggr;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import se.sundsvall.archive.api.domain.ArchiveResponse;
import se.sundsvall.archive.integration.formpipeproxy.FormpipeProxyMapper;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

@Component
public class ByggRFormpipeProxyMapper implements FormpipeProxyMapper<ByggRArchiveRequest, ArchiveResponse> {

    private final String submissionAgreementId;
    private final MetadataValidator metadataValidator;

    public ByggRFormpipeProxyMapper(
            @Value("${byggr.submission-agreement-id}") final String submissionAgreementId,
            final MetadataValidator metadataValidator) {
        this.submissionAgreementId = submissionAgreementId;
        this.metadataValidator = metadataValidator;
    }

    @Override
    public ImportRequest map(final ByggRArchiveRequest byggRArchiveRequest) {
        if (!metadataValidator.isValidMetadata(byggRArchiveRequest.getMetadata())) {
            throw Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withTitle("Invalid metadata")
                .withDetail("Metadata is missing file extension(s)")
                .build();
        }

        return ImportRequest.builder()
            .withSubmissionAgreementId(submissionAgreementId)
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
