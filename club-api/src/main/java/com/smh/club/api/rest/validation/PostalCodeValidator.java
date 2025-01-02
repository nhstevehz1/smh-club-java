package com.smh.club.api.rest.validation;

import com.smh.club.api.rest.validation.constraints.PostalCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PostalCodeValidator implements ConstraintValidator<PostalCode, String> {

  private String pattern;

  @Override
  public void initialize(PostalCode constraintAnnotation) {
    this.pattern = constraintAnnotation.pattern();
  }

  @Override
  public boolean isValid(String postalCode, ConstraintValidatorContext constraintValidatorContext) {
    // Return true if null.  The NotBlank validator will test for null and empty.
    return postalCode == null || Pattern.compile(pattern).matcher(postalCode).matches();
  }
}
