package edu.cnm.deepdive.qod;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class PreloadLauncher {

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .sources(QuoteOfTheDayApplication.class)
        .profiles("preload")
        .run(args);
  }

}
