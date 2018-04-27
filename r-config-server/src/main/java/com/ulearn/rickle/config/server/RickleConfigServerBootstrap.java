package com.ulearn.rickle.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Created by Sunand on 13/04/18.
 */
@EnableConfigServer
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class RickleConfigServerBootstrap {

  public static void main(String... args) {
    SpringApplication.run(RickleConfigServerBootstrap.class, args);
  }
}
