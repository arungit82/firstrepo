package com.carnival.mm;

import com.carnival.mm.service.MedallionChannels;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Import;

/**
 * This Spring Boot Application class serves as the entry point for bringing up the application
 */
@SpringBootApplication
//@EnableBinding(MedallionChannels.class)
@Import({DatabaseConfig.class, SwaggerConfig.class})
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }


    /**
     * Main method for starting up the Spring Boot Application
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}