package com.springbootTP.springbootPostgreSQL.principale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.springbootTP.springbootPostgreSQL.backend.repository")
@ComponentScan(basePackages = "com.springbootTP.springbootPostgreSQL.backend")
@EntityScan(basePackages = {"com.springbootTP.springbootPostgreSQL.backend"})
public class SpringbootPostgreSqlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootPostgreSqlApplication.class, args);
	}

}
