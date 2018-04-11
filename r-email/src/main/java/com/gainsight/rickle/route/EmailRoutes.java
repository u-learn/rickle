package com.gainsight.rickle.route;

import com.gainsight.rickle.handler.EmailHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

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
            .andNest(path("/email"), RouterFunctions.route(RequestPredicates.all(), handler::hello))
            .andOther(RouterFunctions.route(RequestPredicates.all(), handler::other));
  }
}
