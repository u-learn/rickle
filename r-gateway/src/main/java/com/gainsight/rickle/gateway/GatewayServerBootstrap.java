package com.gainsight.rickle.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Created by Sunand on 08/04/18.
 */
@EnableReactiveMongoRepositories
@EnableWebFluxSecurity
@EnableScheduling
@SpringBootApplication
public class GatewayServerBootstrap {

  @Autowired
  private ReactiveMongoOperations mongoOperations;

  @Bean
  public MongoRouteDefinitionRepository routeDefinitionRepository() {
    return new MongoRouteDefinitionRepository();
  }

  /*@Bean
  public RouteLocator routeLocator(RouteLocatorBuilder builder) {

    return builder.routes()
            .route("example", r -> r.host("localhost:8080")
                    .and()
                      .path("/ant/**")
                      .filters(f -> f.addResponseHeader("X-TestHeader", "foobar"))
//                              .setPath("/{segment}"))
                      .uri("http://httpbin.org/html")
            )
            .route("sunand",
                    predicateSpec -> predicateSpec.path("/sunand/**")
                    .filters(f ->
                            f.rewritePath("/sunand/(?<segment>.*)", "/sunand/${segment}")
                             .addRequestHeader("version", "v3")
                    )
                    .uri("http://httpbin.org")
            )
            .route("test",
                    spec -> spec.path("/test/**")
                    .filters(f1 -> f1.filter(new MyGatewayFilter()))
                    .uri("http://httpbin.org/html")
            )
//            .route(oneRoute.getName(), r -> r.path(oneRoute.getContext()).uri(oneRoute.getTo()))
            .build();

  }*/

  @Bean
  public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
    return http
            // Demonstrate that method security works
            // Best practice to use both for defense in depth
            .authorizeExchange()
            .anyExchange().permitAll()
            .and()
            .httpBasic().disable().build();
//            .and().build();
//    return http.build();
  }

  public static void main(String[] args) {
    SpringApplication.run(GatewayServerBootstrap.class, args);
  }
}