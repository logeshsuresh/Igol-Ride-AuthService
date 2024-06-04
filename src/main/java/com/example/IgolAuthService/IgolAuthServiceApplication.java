package com.example.IgolAuthService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IgolAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgolAuthServiceApplication.class, args);
	}

}
