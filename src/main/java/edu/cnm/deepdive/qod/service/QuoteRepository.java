package edu.cnm.deepdive.qod.service;

import edu.cnm.deepdive.qod.model.entity.Quote;
import edu.cnm.deepdive.qod.model.entity.Source;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuoteRepository extends JpaRepository<Quote, UUID> {

  Iterable<Quote> getAllByOrderByCreatedDesc();

  Iterable<Quote> getAllByOrderByTextAsc();

  Iterable<Quote> getAllByTextContainsOrderByTextAsc(String fragment);

  @Query(value = "SELECT * FROM sa.Quote ORDER BY RANDOM() OFFSET 0 ROWS FETCH NEXT 1 ROW ONLY",
      nativeQuery = true)
  Optional<Quote> getRandom();

  @Query(value = "SELECT COUNT(*) FROM sa.Quote", nativeQuery = true)
  long getCount();

  @Query(value = "SELECT * FROM sa.Quote OFFSET ?1 ROWS FETCH NEXT ROW ONLY", nativeQuery = true)
  Optional<Quote> getQuoteOfDay(long dayOffset);

  Iterable<Quote> getAllBySourceOrderByTextAsc(Source source);

  default Quote findOrFail(UUID id) {
    return findById(id).get();
  }

}
