package com.ulearn.rickle.route;

import com.ulearn.rickle.handler.EmailHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Created by Sunand on 11/04/18.
 */
@Configuration
public class EmailRoutes {

  @Bean
  public RouterFunction<?> route(EmailHandler handler) {
    return RouterFunctions.
            route(GET("/parse/").and(accept(MediaType.ALL)), handler::hello)
            .andRoute(GET("/hello").and(accept(MediaType.ALL)), handler::hello)
            .andRoute(GET("/routes").and(accept(MediaType.APPLICATION_JSON)), handler::routes)
            .andOther(RouterFunctions.route(RequestPredicates.all(), handler::other));
  }
}
