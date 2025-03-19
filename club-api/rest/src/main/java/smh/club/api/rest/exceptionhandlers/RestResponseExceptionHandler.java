package smh.club.api.rest.exceptionhandlers;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
  }

  @Override
  protected ResponseEntity<Object>  handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                 @NonNull HttpHeaders headers,
                                                                 @NonNull HttpStatusCode status,
                                                                 @NonNull WebRequest request) {

    if (!ex.getBindingResult().getFieldErrors().isEmpty()) {
      var fieldErrors =
          ex.getBindingResult().getFieldErrors().stream()
              .map(v -> ValidationError.of(v.getField(), v.getDefaultMessage()))
              .toList();
      return handleExceptionInternal(ex, ValidationErrorResponse.of(fieldErrors), headers, status, request);
    } else {
      var errors =
          ex.getBindingResult().getAllErrors().stream()
              .map(v -> ValidationError.of(null, v.getDefaultMessage()))
              .toList();
      return handleExceptionInternal(ex, ValidationErrorResponse.of(errors), headers, status, request);
    }
  }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> onException(RuntimeException ex) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
