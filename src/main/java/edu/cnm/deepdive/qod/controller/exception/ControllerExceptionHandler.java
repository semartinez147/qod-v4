package edu.cnm.deepdive.qod.controller.exception;

import edu.cnm.deepdive.qod.model.dto.ErrorResponse;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

  private static final String CONSTRAINT_VIOLATIONS_MESSAGE = "One or more constraints violated in request parameters";

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> badRequestContent(
      ConstraintViolationException ex, WebRequest request) {
    ErrorResponse body = new ErrorResponse(HttpStatus.BAD_REQUEST,
        CONSTRAINT_VIOLATIONS_MESSAGE, ((ServletWebRequest) request).getRequest().getRequestURI(),
        ex.getConstraintViolations().stream()
            .map((v) -> String.format("%1$s: %2$s", v.getPropertyPath(), v.getMessage()))
            .collect(Collectors.toList()));
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
  public void notFound() {
  }

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid request content or parameters")
  public void badRequest() {
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Duplicated resource")
  public void duplicatedResource() {
  }

}
