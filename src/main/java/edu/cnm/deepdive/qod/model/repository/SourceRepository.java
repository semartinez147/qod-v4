package edu.cnm.deepdive.qod.model.repository;

import edu.cnm.deepdive.qod.model.entity.Source;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SourceRepository extends JpaRepository<Source, UUID> {

  Iterable<Source> findAllByOrderByName();

  @Query("SELECT DISTINCT s FROM Source AS s WHERE EXISTS (SELECT q FROM Quote AS q WHERE q.source = s) ORDER BY s.name")
  Iterable<Source> findAllWithQuotesOrderByName();

  Iterable<Source> getAllByNameContainsOrderByNameAsc(String fragment);

}
