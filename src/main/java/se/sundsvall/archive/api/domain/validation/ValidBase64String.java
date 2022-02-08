package se.sundsvall.archive.api.domain.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({
    ElementType.FIELD,
    ElementType.CONSTRUCTOR,
    ElementType.PARAMETER
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidBase64StringConstraintValidator.class)
public @interface ValidBase64String {

    String message() default "must be a BASE64-encoded string";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
