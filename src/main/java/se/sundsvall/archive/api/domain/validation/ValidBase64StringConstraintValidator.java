package se.sundsvall.archive.api.domain.validation;

import java.util.Base64;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;

public class ValidBase64StringConstraintValidator
        implements ConstraintValidator<ValidBase64String, String> {

    @Override
    public boolean isValid(final String s, final ConstraintValidatorContext context) {
        if (StringUtils.isBlank(s)) {
            return false;
        }

        try {
            Base64.getDecoder().decode(s);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
