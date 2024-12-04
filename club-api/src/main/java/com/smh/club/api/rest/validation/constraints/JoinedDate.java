package com.smh.club.api.rest.validation.constraints;

import com.smh.club.api.rest.validation.JoinedDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

/**
 * A constraint validator used on LocalDate fields.
 * Instructs validation to fire the {@link JoinedDateValidator} validator.
 */
@Constraint(validatedBy = JoinedDateValidator.class)
@Target({ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface JoinedDate {
  Class<?> value() default LocalDate.class;
  String message () default "Invalid joined date.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
