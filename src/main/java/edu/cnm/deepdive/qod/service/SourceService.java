package edu.cnm.deepdive.qod.service;

import edu.cnm.deepdive.qod.model.entity.Quote;
import edu.cnm.deepdive.qod.model.entity.Source;
import edu.cnm.deepdive.qod.model.repository.QuoteRepository;
import edu.cnm.deepdive.qod.model.repository.SourceRepository;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SourceService {

  private final QuoteRepository quoteRepository;
  private final SourceRepository sourceRepository;

  @Autowired
  public SourceService(QuoteRepository quoteRepository,
      SourceRepository sourceRepository) {
    this.quoteRepository = quoteRepository;
    this.sourceRepository = sourceRepository;
  }

  public Source create(Source source) {
    return sourceRepository.save(source);
  }

  public Iterable<Source> readAll(boolean includeNull, boolean includeEmpty) {
    Iterable<Source> raw = includeEmpty
        ? sourceRepository.findAllByOrderByName()
        : sourceRepository.findAllWithQuotesOrderByName();
    if (includeNull) {
      List<Source> sources = new LinkedList<>();
      for (Source source : raw) {
        sources.add(source);
      }
      Source nullSource = new Source();
      for (Quote quote : quoteRepository.getAllBySourceOrderByTextAsc(null)) {
        nullSource.getQuotes().add(quote);
      }
      if (!nullSource.getQuotes().isEmpty()) {
        sources.add(nullSource);
      }
      return sources;
    } else {
      return raw;
    }
  }

  public Iterable<Source> readFiltered(String fragment) {
    return sourceRepository.getAllByNameContainsOrderByNameAsc(fragment);
  }

  public Optional<Source> readOne(UUID id) {
    return sourceRepository.findById(id);
  }

  public Optional<Source> update(UUID id, Source source) {
    return sourceRepository.findById(id).map((s) -> {
      s.setName(source.getName());
      return sourceRepository.save(s);
    });
  }

  public Optional<String> update(UUID id, String name) {
    return sourceRepository.findById(id).map((s) -> {
      s.setName(name);
      return sourceRepository.save(s).getName();
    });
  }

  public void delete(UUID id) {
    sourceRepository.findById(id).ifPresent((source) -> {
      Set<Quote> quotes = source.getQuotes();
      quotes.forEach((quote) -> quote.setSource(null));
      quotes.clear();
      sourceRepository.delete(source);
    });
  }

}
