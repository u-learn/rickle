package com.gainsight.rickle.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by Sunand on 10/04/18.
 */
public class MyGatewayFilter implements GatewayFilter {
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    exchange.getFormData();
    System.out.println(exchange.getRequest().getQueryParams());
    return chain.filter(exchange);
  }
}
