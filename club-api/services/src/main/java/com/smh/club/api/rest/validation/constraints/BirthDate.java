package com.smh.club.api.rest.validation.constraints;

import com.smh.club.api.rest.validation.BirthDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Instant;

/**
 * A constraint validator used on LocalDate fields.
 * Instructs validation to fire the {@link BirthDateValidator} validator.
 */
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDate {
  Class<?> value() default Instant.class;

  int minAge() default 21;
  String message() default "Birth date must be at least 21 years in the past";

  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
