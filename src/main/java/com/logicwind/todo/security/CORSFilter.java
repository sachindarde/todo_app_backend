package com.logicwind.todo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class CORSFilter implements WebFluxConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins(
				
				"http://localhost:3000",
				"http://localhost:8081",
				"http://localhost:8082",
				
				"https://operational.codeapprun.io",
				"https://widget.codeapprun.io",
				"https://www.codeapprun.io",
				"https://codeapprun.io",
				"https://storage.googleapis.com",
				
				"http://operational.codeapprun.io",
				"http://widget.codeapprun.io",
				"http://www.codeapprun.io",
				"http://codeapprun.io",
				"http://storage.googleapis.com"
				
				).allowedMethods("*").allowedHeaders("*");
	}
}