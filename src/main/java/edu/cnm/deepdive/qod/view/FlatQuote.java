package edu.cnm.deepdive.qod.view;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URI;
import java.util.Date;
import java.util.UUID;
import org.springframework.lang.NonNull;

@JsonPropertyOrder({"id", "created", "updated", "text", "href"})
public interface FlatQuote {

  UUID getId();

  Date getCreated();

  Date getUpdated();

  @NonNull
  String getText();

  URI getHref();

}
