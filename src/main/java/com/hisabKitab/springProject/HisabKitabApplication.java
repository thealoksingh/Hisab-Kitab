package com.hisabKitab.springProject;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@SpringBootApplication
public class HisabKitabApplication {

	private static final Logger logger = LoggerFactory.getLogger(HisabKitabApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(HisabKitabApplication.class, args);

		logger.info("Server Started Successfully..");

		
	}
}
