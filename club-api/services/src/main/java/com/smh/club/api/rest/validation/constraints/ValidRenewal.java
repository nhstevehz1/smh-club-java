package com.smh.club.api.rest.validation.constraints;

import com.smh.club.api.rest.validation.RenewalValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A constraint validator used on LocalDate fields.
 * Instructs validation to fire the {@link RenewalValidator} validator.
 */
@Constraint(validatedBy = RenewalValidator.class)
@Target({ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRenewal {
  String message () default "Invalid Renewal year. Must be year of renewal date or previous year.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
