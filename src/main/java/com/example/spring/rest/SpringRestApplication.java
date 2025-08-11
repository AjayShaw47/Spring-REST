package com.example.spring.rest;

import com.example.spring.rest.services.UserService;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringRestApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringRestApplication.class, args);
		UserService userService = context.getBean(UserService.class);


	}

}
