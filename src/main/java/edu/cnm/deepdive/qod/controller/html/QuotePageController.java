package edu.cnm.deepdive.qod.controller.html;

import edu.cnm.deepdive.qod.service.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/quotes")
public class QuotePageController {

  private final QuoteRepository repository;

  @Autowired
  public QuotePageController(QuoteRepository repository) {
    this.repository = repository;
  }

  @GetMapping(value = "/random", produces = MediaType.TEXT_HTML_VALUE)
  public String getRandom(Model model) {
    model.addAttribute("quote", repository.getRandom().get());
    return "random";
  }

  @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
  public String getAll(Model model) {
    model.addAttribute("quotes", repository.getAllByOrderByCreatedDesc());
    return "list";
  }

}
