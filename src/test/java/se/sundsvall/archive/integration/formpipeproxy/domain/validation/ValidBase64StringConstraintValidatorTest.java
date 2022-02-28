package se.sundsvall.archive.integration.formpipeproxy.domain.validation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import se.sundsvall.archive.api.domain.validation.ValidBase64StringConstraintValidator;

@ActiveProfiles("junit")
class ValidBase64StringConstraintValidatorTest {

    private final ValidBase64StringConstraintValidator validator = new ValidBase64StringConstraintValidator();

    @Test
    void test_nullInputReturnsFalse() {
        assertThat(validator.isValid(null, null)).isFalse();
    }

    @Test
    void test_blankInputReturnsFalse() {
        assertThat(validator.isValid("", null)).isFalse();
    }

    @Test
    void test_whitespaceOnlyInputReturnsFalse() {
        assertThat(validator.isValid("   ", null)).isFalse();
    }

    @Test
    void test_invalidBase64Input_returnsFalse() {
        assertThat(validator.isValid("i-am-not-base-64", null)).isFalse();
    }

    @Test
    void test_validBase64Input_returnsFalse() {
        assertThat(validator.isValid("dGVzdGluZyB0ZXN0aW5nCg==", null)).isTrue();
    }
}
