package com.grpc.credibanco;

import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CredibanCo {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(CredibanCo.class);
        springApplication.run(args);
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }
}
