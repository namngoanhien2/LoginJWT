package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.demo"})
public class DemoSpringJwt2Application {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringJwt2Application.class, args);
	}

}
