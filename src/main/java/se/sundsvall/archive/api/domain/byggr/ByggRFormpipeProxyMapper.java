package se.sundsvall.archive.api.domain.byggr;

import java.util.Optional;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import se.sundsvall.archive.api.domain.ArchiveResponse;
import se.sundsvall.archive.api.exception.ArchiveException;
import se.sundsvall.archive.integration.formpipeproxy.FormpipeProxyMapper;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportRequest;
import se.sundsvall.archive.integration.formpipeproxy.domain.ImportResponse;

@Component
public class ByggRFormpipeProxyMapper implements FormpipeProxyMapper<ByggRArchiveRequest, ArchiveResponse> {

	private static final String HANDLINGSTYP_XPATH = "//ArkivobjektHandling/Handlingstyp/text()";

	private final DocumentBuilder documentBuilder;
	private final XPath xPath;

	private final String submissionAgreementId;
	private final MetadataUtil metadataUtil;

	public ByggRFormpipeProxyMapper(
		@Value("${byggr.submission-agreement-id}") final String submissionAgreementId,
		final MetadataUtil metadataUtil) {
		this.submissionAgreementId = submissionAgreementId;
		this.metadataUtil = metadataUtil;

		try {
			final var documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderFactory.newDocumentBuilder();

			final var xPathFactory = XPathFactory.newInstance();
			xPath = xPathFactory.newXPath();
		} catch (final Exception e) {
			// This should never ever happen, but still...
			throw new ArchiveException("Unable to create document builder", e);
		}
	}

	@Override
	public ImportRequest map(final ByggRArchiveRequest request) {
		if (!metadataUtil.isValidMetadata(request.getMetadata())) {
			throw Problem.builder()
				.withStatus(Status.BAD_REQUEST)
				.withTitle("Invalid metadata")
				.withDetail("Metadata is missing file extension(s)")
				.build();
		}

		final var uuid = UUID.randomUUID().toString();

		return ImportRequest.builder()
			.withSubmissionAgreementId(submissionAgreementId)
			.withUuid(uuid)
			.withMetadataXml(toBase64(metadataUtil.replaceAttachmentNameAndLink(
				request.getMetadata(), uuid, request.getAttachment().getExtension())))
			// .withPersonalDataFlag(false)
			.withConfidentialityLevel(metadataUtil.getConfidentialityLevel(request.getMetadata()))
			// .withConfidentialityDegradationDate(LocalDateTime.now().plusYears(1))
			.withPreservationObject(ImportRequest.PreservationObject.builder()
				.withFileExtension(request.getAttachment().getExtension())
				.withFileName(uuid)
				.withData(request.getAttachment().getFile())
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
