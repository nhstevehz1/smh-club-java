package com.smh.club.api.shared.exceptionhandlers;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationErrorResponse {

  @JsonProperty("validation-errors")
  private final List<ValidationError> validationErrors;

  public static ValidationErrorResponse of(List<ValidationError> validationErrors) {
    return new ValidationErrorResponse(validationErrors);
  }
}
