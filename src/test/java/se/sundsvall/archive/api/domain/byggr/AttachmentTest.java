package se.sundsvall.archive.api.domain.byggr;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.util.Base64Utils.encodeToString;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Consumer;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttachmentTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testGettersAndSetters() {
        var attachment = createAttachment();

        assertThat(attachment.getName()).isEqualTo("someName");
        assertThat(attachment.getExtension()).isEqualTo(".bmp");
        assertThat(attachment.getFile()).isEqualTo(encodeToString("someFileContents".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testValidation_ok() {
        var attachment = createAttachment();
        var constraintViolations = validator.validate(attachment);

        assertThat(constraintViolations).isEmpty();
    }


    @Test
    void testValidation_withNameNull() {
        var attachment = createAttachment(a -> a.setName(null));
        var constraintViolations = List.copyOf(validator.validate(attachment));

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.get(0).getMessageTemplate()).contains("NotBlank");
        assertThat(constraintViolations.get(0).getPropertyPath().toString()).isEqualTo("name");
    }

    @Test
    void testValidation_withNameBlank() {
        var attachment = createAttachment(a -> a.setName(""));
        var constraintViolations = List.copyOf(validator.validate(attachment));

        assertThat(constraintViolations).hasSize(1);
        assertThat(constraintViolations.get(0).getMessageTemplate()).contains("NotBlank");
        assertThat(constraintViolations.get(0).getPropertyPath().toString()).isEqualTo("name");
    }

    private Attachment createAttachment() {
        return createAttachment(null);
    }

    private Attachment createAttachment(final Consumer<Attachment> modifier) {
        var attachment = new Attachment();
        attachment.setName("someName");
        attachment.setExtension(".bmp");
        attachment.setFile(encodeToString("someFileContents".getBytes(StandardCharsets.UTF_8)));

        if (modifier != null) {
            modifier.accept(attachment);
        }

        return attachment;
    }
}
