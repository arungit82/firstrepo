package com.carnival.mm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * This Spring Boot Application class serves as the entry point for bringing up the application
 */
@SpringBootApplication
@Import(DatabaseConfig.class)
public class Application {

    /**
     * Main method for starting up the Spring Boot Application
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}