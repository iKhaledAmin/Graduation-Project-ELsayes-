package com.GP.ELsayes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ELsayesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ELsayesApplication.class, args);
	}

}