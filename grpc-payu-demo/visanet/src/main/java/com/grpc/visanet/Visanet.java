package com.grpc.visanet;

import brave.Tracing;
import brave.grpc.GrpcTracing;
import brave.sampler.Sampler;
import io.grpc.ClientInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Visanet {
    
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Visanet.class);
        springApplication.run(args);
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

    @Bean
    public GrpcTracing grpcTracing(Tracing tracing) {
        return GrpcTracing.create(tracing);
    }

    //We also create a client-side interceptor and put that in the context, this interceptor can then be injected into gRPC clients and
    //then applied to the managed channel.
    @Bean
    ClientInterceptor grpcClientSleuthInterceptor(GrpcTracing grpcTracing) {
        return grpcTracing.newClientInterceptor();
    }

}
