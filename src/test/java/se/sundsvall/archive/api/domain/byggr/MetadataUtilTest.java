package se.sundsvall.archive.api.domain.byggr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;

@ExtendWith(ResourceLoaderExtension.class)
class MetadataUtilTest {

	private final MetadataUtil util = new MetadataUtil();

	@Test
	void isValidMetadataValid(@Load("/metadata/metadata.valid.xml") final String metadataXml) {
		assertThat(util.isValidMetadata(metadataXml)).isTrue();
	}

	@Test
	void isValidMetadataInvalid(@Load("/metadata/metadata.invalid.xml") final String metadataXml) {
		assertThat(util.isValidMetadata(metadataXml)).isFalse();
	}

	@Test
	void getConfidentialityLevelOkLevel0(@Load("/metadata/metadata.valid.confidentialitylevel0.xml") final String metadataXml) {
		assertThat(util.getConfidentialityLevel(metadataXml)).isZero();
	}

	@Test
	void getConfidentialityLevelOKLevel1(@Load("/metadata/metadata.valid.confidentialitylevel1.xml") final String metadataXml) {
		assertThat(util.getConfidentialityLevel(metadataXml)).isOne();
	}

	@Test
	void getConfidentialityLevelInvalidNoConfidentialityLevel(@Load("/metadata/metadata.invalid.no-confidentialitylevels.xml") final String metadataXml) {
		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> util.getConfidentialityLevel(metadataXml))
			.withMessage("Invalid metadata: Unable to extract 'Handlingstyp' from metadata. Found 0 matching node(s)");
	}

	@Test
	void getConfidentialityLevelInvalidMultipleConfidentialityLevels(@Load("/metadata/metadata.invalid.multiple-confidentialitylevels.xml") final String metadataXml) {
		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> util.getConfidentialityLevel(metadataXml))
			.withMessage("Invalid metadata: Unable to extract 'Handlingstyp' from metadata. Found 2 matching node(s)");
	}

	@Test
	void replaceAttachmentNameAndLink(@Load("/metadata/metadata.valid.xml") final String metadataXml) {
		final var modifiedMetadataXml = util.replaceAttachmentNameAndLink(metadataXml, "someUuid", ".xyz");

		assertThat(modifiedMetadataXml).contains("someUuid.xyz");
	}

	@Test
	void replaceAttachmentNameAndLinkInvalidMultipleAttachments(@Load("/metadata/metadata.invalid.multiple-attachments.xml") final String metadataXml) {
		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> util.replaceAttachmentNameAndLink(metadataXml, "someUuid", ".xyz"))
			.withMessage("Invalid metadata: Found 2 'Bilaga' node(s), when 1 was expected");
	}
}
