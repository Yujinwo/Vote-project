package com.react.voteproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class VoteprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoteprojectApplication.class, args);
	}

}
