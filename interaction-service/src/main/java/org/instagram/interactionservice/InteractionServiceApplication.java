package org.instagram.interactionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class InteractionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InteractionServiceApplication.class, args);
        System.out.println("\n--- Interaction Service Started Successfully ---");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
