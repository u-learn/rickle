package com.gainsight.rickle.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.data.mongodb.core.MongoOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sunand on 09/04/18.
 */
//@Component
public class MongoRouteDefinitionRepository implements RouteDefinitionRepository {
  private final Map<String, RouteDefinition> routes = Collections.synchronizedMap(new LinkedHashMap());

  @Autowired
  private MongoOperations mongoOperations;

  @Override
  public Flux<RouteDefinition> getRouteDefinitions() {
    List<Routes> routes = mongoOperations.findAll(Routes.class);
    System.out.println("I AM SUNAND : " + routes);
    /*for (Routes route : routes) {
      RouteDefinition routeDefinition = new RouteDefinition();
      routeDefinition.setId(route.getName());
      URI uri = null;
      try {
        uri = new URIBuilder(route.getTo()).build();
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
      routeDefinition.setUri(uri);
//      PredicateDefinition hostPredicate = new PredicateDefinition();
//      PathRoutePredicateFactory factory = new PathRoutePredicateFactory();
//      routeDefinition.setPredicates();
      this.routes.put(route.getName(), routeDefinition);
    }*/

    return Flux.fromIterable(this.routes.values());
  }

  public Mono<Void> save(Mono<RouteDefinition> route) {
    System.out.println("SAVE MODULE");
    return route.flatMap((r) -> {
      this.routes.put(r.getId(), r);
      return Mono.empty();
    });
  }

  public Mono<Void> delete(Mono<String> routeId) {
    return routeId.flatMap((id) -> {
      if (this.routes.containsKey(id)) {
        this.routes.remove(id);
        return Mono.empty();
      } else {
        return Mono.defer(() -> Mono.error(new NotFoundException("RouteDefinition not found: " + routeId)));
      }
    });
  }
}
