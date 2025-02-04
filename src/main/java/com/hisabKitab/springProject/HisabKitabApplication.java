package com.hisabKitab.springProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class HisabKitabApplication {


	public static void main(String[] args) {
		 Dotenv dotenv = Dotenv.configure()
	                .directory("etc/secrets/")  // Point to the correct path
	                .load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(HisabKitabApplication.class, args);

		System.out.println("Server Started Successfully..");

		
	}
}
