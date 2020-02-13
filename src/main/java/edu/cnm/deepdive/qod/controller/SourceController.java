package edu.cnm.deepdive.qod.controller;

import edu.cnm.deepdive.qod.model.entity.Source;
import edu.cnm.deepdive.qod.service.SourceRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sources")
public class SourceController {

  private final SourceRepository repository;

  @Autowired
  public SourceController(SourceRepository repository) {
    this.repository = repository;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Source post(@RequestBody Source source) {
    return repository.save(source);
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Source> get() {
    return repository.findAllByOrderByName();
  }

  @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Source get(@PathVariable UUID id) {
    return repository.findById(id).get();
  }

}
