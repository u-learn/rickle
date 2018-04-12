package com.gainsight.rickle.handler;

import com.gainsight.rickle.domain.Route;
import com.gainsight.rickle.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Created by Sunand on 12/04/18.
 */
@Service
public class EmailHandler {

  @Autowired
  private RouteRepository routeRepository;

  public Mono<ServerResponse> hello(ServerRequest request) {
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject("Hello World"));
  }

  public Mono<ServerResponse> other(ServerRequest request) {
    return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromObject("UNKNOWN ROUTE"));
  }

  public Mono<ServerResponse> routes(ServerRequest request) {
    return ServerResponse.ok()
            .body(routeRepository.findAll(), Route.class);
  }
}
