package com.smh.club.api.shared.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import com.smh.club.api.shared.annotations.ZipCode;

public class ZipCodeValidator implements ConstraintValidator<ZipCode, String> {

  private static final String ZIP_REGEX = "^[0-9]{5}(?:-[0-9]{4})?$";

  @Override
  public boolean isValid(String zipCode, ConstraintValidatorContext constraintValidatorContext) {
    return zipCode != null && Pattern.compile(ZIP_REGEX).matcher(zipCode).matches();
  }
}
