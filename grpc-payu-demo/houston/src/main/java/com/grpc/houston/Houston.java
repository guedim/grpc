package com.grpc.houston;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Houston {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Houston.class);
        springApplication.run(args);

    }
}
