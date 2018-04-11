package com.gainsight.rickle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
public class RickleApplication {

	public static void main(String[] args) {
		SpringApplication.run(RickleApplication.class, args);
	}

	@Bean
	public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
		return http
					.authorizeExchange()
					.anyExchange().permitAll()
					.and()
					.httpBasic().disable().build();
	}
}
