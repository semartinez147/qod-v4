package edu.cnm.deepdive.qod.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cnm.deepdive.qod.model.entity.Quote;
import edu.cnm.deepdive.qod.model.entity.Source;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Profile("preload")
public class Preloader implements CommandLineRunner {

  private static final String PRELOAD_DATA = "preload/quotes.json";

  private final QuoteService quoteService;
  private final SourceService sourceService;

  @Autowired
  public Preloader(QuoteService quoteService,
      SourceService sourceService) {
    this.quoteService = quoteService;
    this.sourceService = sourceService;
  }

  @Override
  public void run(String... args) throws Exception {
    ClassPathResource resource = new ClassPathResource(PRELOAD_DATA);
    try (InputStream input = resource.getInputStream()) {
      ObjectMapper mapper = new ObjectMapper();
      Map<String, UUID> sources = new HashMap<>();
      Quote[] quotes = mapper.readValue(input, Quote[].class);
      for (Quote quote : quotes) {
        String sourceName = quote.getSource().getName();
        UUID sourceId = sources.get(sourceName);
        boolean addToSource = true;
        if (sourceId != null) {
          Source source = sourceService.readOne(sourceId).get();
          quote.setSource(source);
          addToSource = false;
        }
        quoteService.create(quote);
        if (addToSource) {
          sources.put(sourceName, quote.getSource().getId());
        }
      }
    }
  }

}
