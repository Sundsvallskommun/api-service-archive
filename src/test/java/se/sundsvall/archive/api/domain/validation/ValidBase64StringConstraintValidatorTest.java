package se.sundsvall.archive.api.domain.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ValidBase64StringConstraintValidatorTest {

    private ValidBase64StringConstraintValidator validator = new ValidBase64StringConstraintValidator();

    @Test
    void test_isValid_withNullInput() {
        assertThat(validator.isValid(null, null)).isFalse();
    }

    @Test
    void test_isValid_withBlankInput() {
        assertThat(validator.isValid("", null)).isFalse();
    }

    @Test
    void test_isValid_withNonBase64Input() {
        assertThat(validator.isValid("i am not base64", null)).isFalse();
    }

    @Test
    void test_isValid_withBase64Input() {
        assertThat(validator.isValid("YXBhCg==", null)).isTrue();
    }
}
