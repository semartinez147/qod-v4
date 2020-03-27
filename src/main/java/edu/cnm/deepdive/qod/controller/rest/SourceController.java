package edu.cnm.deepdive.qod.controller.rest;

import edu.cnm.deepdive.qod.model.entity.Quote;
import edu.cnm.deepdive.qod.model.entity.Source;
import edu.cnm.deepdive.qod.model.repository.QuoteRepository;
import edu.cnm.deepdive.qod.model.repository.SourceRepository;
import edu.cnm.deepdive.qod.service.SourceService;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sources")
@ExposesResourceFor(Source.class)
@Validated
public class SourceController {

  private final SourceService sourceService;

  @Autowired
  public SourceController(SourceService sourceService) {
    this.sourceService = sourceService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Source> post(@RequestBody @Valid Source source) {
    source = sourceService.create(source);
    return ResponseEntity.created(source.getHref()).body(source);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Source> get(
      @RequestParam(required = false, defaultValue = "false") boolean includeNull,
      @RequestParam(required = false, defaultValue = "true") boolean includeEmpty) {
    return sourceService.readAll(includeNull, includeEmpty);
  }

  @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Source> search(@RequestParam @Size(min = 3) String q) {
    return sourceService.readFiltered(q);
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Source> get(@PathVariable UUID id) {
    return ResponseEntity.of(sourceService.readOne(id));
  }

  @PutMapping(value = "/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Source> put(@PathVariable UUID id, @RequestBody @Valid Source source) {
    return ResponseEntity.of(sourceService.update(id, source));
  }

  @GetMapping(value = "/{id}/name", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> getName(@PathVariable UUID id) {
    return ResponseEntity.of(sourceService.readOne(id).map(Source::getName));
  }

  @PutMapping(value = "/{id}/name",
      consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> putName(@PathVariable UUID id, @RequestBody @NotBlank String updatedName) {
    return ResponseEntity.of(sourceService.update(id, updatedName));
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable UUID id) {
    sourceService.delete(id);
  }

}
