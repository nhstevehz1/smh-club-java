package com.smh.club.api.rest.exceptionhandlers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.smh.club.api.shared.exceptionhandlers.ValidationError;
import com.smh.club.api.shared.exceptionhandlers.ValidationErrorResponse;

@ControllerAdvice
public class RestResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse>  handleConstraintViolation(ConstraintViolationException ex) {
      var violations =
          ex.getConstraintViolations().stream()
              .map(v -> ValidationError.of(v.getPropertyPath().toString(), v.getMessage()))
              .toList();
      return ResponseEntity.badRequest().body(ValidationErrorResponse.of(violations));
    }

  @Override
  protected ResponseEntity<Object>  handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                 @NonNull HttpHeaders headers,
                                                                 @NonNull HttpStatusCode status,
                                                                 @NonNull WebRequest request) {

      var violations =
        ex.getBindingResult().getFieldErrors().stream()
            .map(v -> ValidationError.of(v.getField(), v.getDefaultMessage()))
            .toList();

    return handleExceptionInternal(ex, ValidationErrorResponse.of(violations), headers, status, request);
  }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> onException(RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
