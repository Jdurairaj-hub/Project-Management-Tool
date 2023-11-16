package com0.G22Trello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class G22TreloApplication {

	public static void main(String[] args) {
		SpringApplication.run(G22TreloApplication.class, args);
	}

}
