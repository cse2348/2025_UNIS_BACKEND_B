package com.example.backend_B;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class BackendBApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendBApplication.class, args);
	}

}
