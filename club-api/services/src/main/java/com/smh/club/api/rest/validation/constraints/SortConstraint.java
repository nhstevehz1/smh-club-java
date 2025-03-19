package com.smh.club.api.rest.validation.constraints;

import com.smh.club.api.rest.validation.SortValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A validator constraint annotation used on a Pageable method parameter.
 * Instructs validation to fire the {@link SortValidator} validator.
 */
@Constraint(validatedBy = SortValidator.class)
@Target( { ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface SortConstraint {
  Class<?> value();
  String message() default "Sort contains an invalid column name.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
