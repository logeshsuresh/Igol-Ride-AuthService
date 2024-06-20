package com.example.IgolAuthService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan("com.example.IgolEntityService.models")
public class IgolAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgolAuthServiceApplication.class, args);
	}

}
