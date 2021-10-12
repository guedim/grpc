package com.grpc.responsecode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResponseCode {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ResponseCode.class);
        springApplication.run(args);
    }
}
