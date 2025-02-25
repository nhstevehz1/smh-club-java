package com.smh.club.api.rest.validation.constraints;

import com.smh.club.api.rest.validation.MemberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

/**
 * A constraint validator used on LocalDate fields.
 * Instructs validation to fire the {@link MemberValidator} validator.
 */
@Constraint(validatedBy = MemberValidator.class)
@Target({ElementType.TYPE_USE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMember {
  Class<?> value() default LocalDate.class;
  String message () default "Invalid Member data.  Joined date must at lease minAge after birth date";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
