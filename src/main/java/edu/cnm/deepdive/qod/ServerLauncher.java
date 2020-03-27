package edu.cnm.deepdive.qod;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class ServerLauncher {

  public static void main(String[] args) {
    new SpringApplicationBuilder()
        .sources(QuoteOfTheDayApplication.class)
        .profiles("server")
        .run(args);
  }

}
