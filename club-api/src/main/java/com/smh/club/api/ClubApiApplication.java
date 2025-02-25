package com.smh.club.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ClubApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClubApiApplication.class, args);
	}

}
