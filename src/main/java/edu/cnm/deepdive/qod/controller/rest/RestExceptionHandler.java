package edu.cnm.deepdive.qod.controller.rest;

import edu.cnm.deepdive.qod.controller.SearchTermTooShortException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(SearchTermTooShortException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Search term too short")
  public void tooShort() {}

  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
  public void notFound() {}

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public void badRequest() {}

}
