package com.grpc.responsecode;

import brave.Tracing;
import brave.grpc.GrpcTracing;
import brave.sampler.Sampler;
import io.grpc.ServerInterceptor;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ResponseCode {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ResponseCode.class);
        springApplication.run(args);
    }

    @Bean
    public Sampler defaultSampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

    //grpc-spring-boot-starter provides @GrpcGlobalInterceptor to allow server-side interceptors to be registered with all
    //server stubs, we are just taking advantage of that to install the server-side gRPC tracer.
    @Bean
    @GRpcGlobalInterceptor
    ServerInterceptor grpcServerSleuthInterceptor(GrpcTracing grpcTracing) {
        return grpcTracing.newServerInterceptor();
    }
}
