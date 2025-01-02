package com.smh.club.api.hateoas.validation.constraints;

import com.smh.club.api.hateoas.validation.PostalCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A constraint validator used on string fields that represent zip codes
 * Instructs validation to fire the {@link PostalCodeValidator} validator.
 */
@Constraint(validatedBy = {PostalCodeValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PostalCode {
  String message() default "Invalid postal code value";
  String pattern() default "^[0-9]{5}(?:-[0-9]{4})?$";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
