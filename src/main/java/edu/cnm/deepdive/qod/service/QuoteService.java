package edu.cnm.deepdive.qod.service;

import edu.cnm.deepdive.qod.model.entity.Quote;
import edu.cnm.deepdive.qod.model.entity.Source;
import edu.cnm.deepdive.qod.model.repository.QuoteRepository;
import edu.cnm.deepdive.qod.model.repository.SourceRepository;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

  private final QuoteRepository quoteRepository;
  private final SourceRepository sourceRepository;

  @Autowired
  public QuoteService(QuoteRepository quoteRepository, SourceRepository sourceRepository) {
    this.quoteRepository = quoteRepository;
    this.sourceRepository = sourceRepository;
  }

  public Quote create(Quote quote) {
    quote.setSource(resolveSource(quote.getSource()));
    return quoteRepository.save(quote);
  }

  public Iterable<Quote> readAll() {
    return quoteRepository.getAllByOrderByTextAsc();
  }

  public Iterable<Quote> readFiltered(String fragment) {
    return quoteRepository.getAllByTextContainsOrderByTextAsc(fragment);
  }

  public Optional<Quote> readRandom() {
    return quoteRepository.getRandom();
  }

  public Optional<Quote> readOne(UUID id) {
    return quoteRepository.findById(id);
  }

  public Optional<Quote> readDaily(LocalDate date) {
    long count = quoteRepository.getCount();
    long dayOffset = date.toEpochDay() % count;
    if (dayOffset < 0) {
      dayOffset += count;
    }
    return quoteRepository.findAll(PageRequest.of((int) dayOffset, 1, Sort.by(Direction.ASC, "id")))
        .get().findFirst();
  }

  public Optional<Quote> update(UUID quoteId, Quote quote) {
    return quoteRepository.findById(quoteId).map((q) -> {
      q.setSource(resolveSource(quote.getSource()));
      q.setText(quote.getText());
      return quoteRepository.save(q);
    });
  }

  public Optional<String> update(UUID quoteId, String text) {
    return quoteRepository.findById(quoteId).map((quote) -> {
      quote.setText(text);
      return quoteRepository.save(quote).getText();
    });
  }

  public Optional<Source> update(UUID quoteId, Source source) {
    return quoteRepository.findById(quoteId).map((quote) -> {
      quote.setSource(resolveSource(source));
      return quoteRepository.save(quote).getSource();
    });
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  public Optional<Source> update(UUID quoteId, UUID sourceId) {
    return quoteRepository.findById(quoteId).map((quote) -> {
      quote.setSource(sourceRepository.findById(sourceId).get());
      return quoteRepository.save(quote).getSource();
    });
  }

  public void clearSource(UUID quoteId) {
    quoteRepository.findById(quoteId).map((quote) -> {
      quote.setSource(null);
      return quoteRepository.save(quote);
    }).get();
  }

  public void delete(UUID id) {
    if (quoteRepository.existsById(id)) {
      quoteRepository.deleteById(id);
    }
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  private Source resolveSource(Source source) {
    UUID sourceId;
    return (source != null && (sourceId = source.getId()) != null)
        ? sourceRepository.findById(sourceId).get()
        : source;
  }

}
