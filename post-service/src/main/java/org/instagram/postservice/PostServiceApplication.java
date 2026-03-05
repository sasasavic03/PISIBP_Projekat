package org.instagram.postservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PostServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(PostServiceApplication.class, args);
        System.out.println("\n--- Post Service Started Successfully ---");
    }


}
