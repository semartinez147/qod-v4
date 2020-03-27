package edu.cnm.deepdive.qod.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;

@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

  private final Date timestamp;
  private final int status;
  private final String error;
  private final String message;
  private final String path;
  private final List<String> details;

  public ErrorResponse(HttpStatus status, String message, String path, List<String> details) {
    this.timestamp = new Date();
    this.status = status.value();
    this.error = status.getReasonPhrase();
    this.message = message;
    this.path = path;
    this.details = details;
  }

  public ErrorResponse(HttpStatus status, String message, String path) {
    this(status, message, path, null);
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public int getStatus() {
    return status;
  }

  public String getError() {
    return error;
  }

  public String getMessage() {
    return message;
  }

  public String getPath() {
    return path;
  }

  public List<String> getDetails() {
    return details;
  }

}
