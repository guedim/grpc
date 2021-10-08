package com.grpc.credibanco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CredibanCo {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CredibanCo.class);
        springApplication.run(args);

    }
}
