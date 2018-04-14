package com.ulearn.rickle.config.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;

/**
 * Created by Sunand on 14/04/18.
 */
@SpringBootApplication(exclude = ReactiveSecurityAutoConfiguration.class)
public class RickleConfigClientBootstrap {

  public static void main(String... args) {
    SpringApplication.run(RickleConfigClientBootstrap.class, args);
  }
}
