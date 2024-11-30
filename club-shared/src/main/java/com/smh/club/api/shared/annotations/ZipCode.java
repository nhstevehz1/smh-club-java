package com.smh.club.api.shared.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.smh.club.api.shared.validators.ZipCodeValidator;

@Constraint(validatedBy = {ZipCodeValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ZipCode {
  String message() default "Invalid Zipcode value";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
