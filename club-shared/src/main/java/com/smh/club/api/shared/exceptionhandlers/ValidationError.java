package com.smh.club.api.shared.exceptionhandlers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationError {

  @JsonProperty("field")
  private final String fieldName;

  @JsonProperty("message")
  private final String message;

  public static ValidationError of(String fieldName, String message) {
    return new ValidationError(fieldName, message);
  }
}
