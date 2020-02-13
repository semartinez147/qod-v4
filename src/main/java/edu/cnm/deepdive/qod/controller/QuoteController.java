package edu.cnm.deepdive.qod.controller;

import edu.cnm.deepdive.qod.model.entity.Quote;
import edu.cnm.deepdive.qod.model.entity.Source;
import edu.cnm.deepdive.qod.service.QuoteRepository;
import edu.cnm.deepdive.qod.service.SourceRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("quotes")
public class QuoteController {

  private final QuoteRepository quoteRepository;
  private final SourceRepository sourceRepository;

  @Autowired
  public QuoteController(QuoteRepository quoteRepository, SourceRepository sourceRepository) {
    this.quoteRepository = quoteRepository;
    this.sourceRepository = sourceRepository;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Quote post(@RequestBody Quote quote) {
    return quoteRepository.save(quote);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Quote> get() {
    return quoteRepository.getAllByOrderByCreatedDesc();
  }

  @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Quote get(@PathVariable UUID id) {
    return quoteRepository.findById(id).get();
  }

  @PutMapping(value = "{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Quote put(@PathVariable UUID id, @RequestBody Quote modifiedQuote) {
    Quote quote = get(id);
    quote.setText(modifiedQuote.getText());
    return quoteRepository.save(quote);
  }

  @PutMapping(value = "{id}/text",
      consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public String put(@PathVariable UUID id, @RequestBody String modifiedQuote) {
    Quote quote = get(id);
    quote.setText(modifiedQuote);
    quoteRepository.save(quote);
    return quote.getText();
  }

  @DeleteMapping(value = "{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    // Code below throws NoSuchElementException if id is not in database.
//    Quote quote = get(id);
//    quoteRepository.delete(quote);
    quoteRepository.findById(id).ifPresent(quoteRepository::delete);
  }

  @PutMapping(value = "{quoteId}/sources/{sourceId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Quote attach(@PathVariable UUID quoteId, @PathVariable UUID sourceId) {
    Quote quote = get(quoteId);
    Source source = sourceRepository.findById(sourceId).get();
    List<Source> sources = quote.getSources();
    boolean matchFound = false;
    for (Source s : sources) {
      if (s.getId().equals(source.getId())) {
        matchFound = true;
        break;
      }
    }
    if (!matchFound) {
      sources.add(source);
      quoteRepository.save(quote);
    }
    return quote;
  }

  @DeleteMapping(value = "{quoteId}/sources/{sourceId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void detach(@PathVariable UUID quoteId, @PathVariable UUID sourceId) {
    Quote quote = get(quoteId);
    Source source = sourceRepository.findById(sourceId).get();
    List<Source> sources = quote.getSources();
    boolean matchFound = false;
    for (Source s : sources) {
      if (s.getId().equals(source.getId())) {
        matchFound = true;
        break;
      }
    }
    if (matchFound) {
      sources.remove(source);
      quoteRepository.save(quote);
    }
  }

}
