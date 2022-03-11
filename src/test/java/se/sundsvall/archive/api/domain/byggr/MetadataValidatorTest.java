package se.sundsvall.archive.api.domain.byggr;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;

@ExtendWith(ResourceLoaderExtension.class)
class MetadataValidatorTest {

    private final MetadataValidator validator = new MetadataValidator();

    @Test
    void test_isValidMetadata_valid(@Load("/metadata/metadata.valid.xml") final String metadataXml) {
        assertThat(validator.isValidMetadata(metadataXml)).isTrue();
    }

    @Test
    void test_isValidMetadata_invalid(@Load("/metadata/metadata.invalid.xml") final String metadataXml) {
        assertThat(validator.isValidMetadata(metadataXml)).isFalse();
    }
}
