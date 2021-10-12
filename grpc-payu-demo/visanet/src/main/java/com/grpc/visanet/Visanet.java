package com.grpc.visanet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Visanet {
    
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Visanet.class);
        springApplication.run(args);

    }
}
