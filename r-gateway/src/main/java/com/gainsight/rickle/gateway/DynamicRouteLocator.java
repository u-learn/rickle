package com.gainsight.rickle.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.handler.predicate.PathRoutePredicateFactory;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.net.URI;

/**
 * Created by Sunand on 10/04/18.
 */
@Slf4j
@Component
public class DynamicRouteLocator implements RouteLocator, ApplicationEventPublisherAware {
  @Autowired
  private ReactiveMongoOperations mongoOperations;

  @Autowired
  ConfigurableApplicationContext applicationContext;

//  @Autowired
//  private RouteLocatorBuilder routeLocatorBuilder;

  ApplicationEventPublisher eventPublisher;

  @Override
  public Flux<Route> getRoutes() {
    return collectRoutes();
  }

  //ToDo: Add RateLimit here per route also define the same in Json
  private Flux<Route> collectRoutes() {
    Flux<Routes> customRoutes = mongoOperations.findAll(Routes.class);
    log.debug("Route List : " + customRoutes.collectList().block());

//    RouteLocatorBuilder.Builder route66 = routeLocatorBuilder.routes();

    Flux<Route> routes = customRoutes.map(route ->
            Route.builder().id(route.getName())
//          .predicate(serverWebExchange -> true)
            .uri(URI.create(route.getTo()))
            .predicate(new PathRoutePredicateFactory().apply(c -> c.setPattern(route.getContext())))
            .build());
//    AnnotationAwareOrderComparator.sort(routes);
    return routes;
  }

//ToDo: Trigger this when there is a change in DB for the route definition

//  @Scheduled(cron = "0/5 * * * * ?")
//  @EventListener(RefreshRoutesEvent.class)
//  public void handleRefresh() {
//    System.out.println("Trigger Happy");
//    eventPublisher.publishEvent(new RefreshRoutesEvent(this));
//  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.eventPublisher = applicationEventPublisher;
  }
}
