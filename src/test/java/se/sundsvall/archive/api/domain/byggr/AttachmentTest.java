package se.sundsvall.archive.api.domain.byggr;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

class AttachmentTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Test
	void testGettersAndSetters() {
		final var attachment = createAttachment();

		assertThat(attachment.getName()).isEqualTo("someName");
		assertThat(attachment.getExtension()).isEqualTo(".bmp");
		assertThat(attachment.getFile()).isEqualTo(encodeToString("someFileContents"));
	}

	@Test
	void testValidation_ok() {
		final var attachment = createAttachment();
		final var constraintViolations = validator.validate(attachment);

		assertThat(constraintViolations).isEmpty();
	}

	@Test
	void testValidation_withNameNull() {
		final var attachment = createAttachment(a -> a.setName(null));
		final var constraintViolations = List.copyOf(validator.validate(attachment));

		assertThat(constraintViolations).hasSize(1);
		assertThat(constraintViolations.get(0).getMessageTemplate()).contains("NotBlank");
		assertThat(constraintViolations.get(0).getPropertyPath()).hasToString("name");
	}

	@Test
	void testValidation_withNameBlank() {
		final var attachment = createAttachment(a -> a.setName(""));
		final var constraintViolations = List.copyOf(validator.validate(attachment));

		assertThat(constraintViolations).hasSize(1);
		assertThat(constraintViolations.get(0).getMessageTemplate()).contains("NotBlank");
		assertThat(constraintViolations.get(0).getPropertyPath()).hasToString("name");
	}

	private Attachment createAttachment() {
		return createAttachment(null);
	}

	private Attachment createAttachment(final Consumer<Attachment> modifier) {
		final var attachment = new Attachment();
		attachment.setName("someName");
		attachment.setExtension(".bmp");
		attachment.setFile(encodeToString("someFileContents"));

		if (modifier != null) {
			modifier.accept(attachment);
		}

		return attachment;
	}

	private String encodeToString(final String s) {
		return Base64.getEncoder().encodeToString(s.getBytes(UTF_8));
	}
}
