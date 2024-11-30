package com.smh.club.api.hateoas.exceptionhandlers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.smh.club.api.shared.exceptionhandlers.ValidationError;
import com.smh.club.api.shared.exceptionhandlers.ValidationErrorResponse;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse>  onConstraintViolationException(ConstraintViolationException ex) {
      var violations =
          ex.getConstraintViolations().stream()
              .map(v -> ValidationError.of(v.getPropertyPath().toString(), v.getMessage()))
              .toList();
      return ResponseEntity.badRequest().body(ValidationErrorResponse.of(violations));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> onException(RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.toString());
    }
}
