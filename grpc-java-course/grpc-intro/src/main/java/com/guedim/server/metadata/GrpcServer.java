package com.guedim.server.metadata;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {

    public static int PORT = 6565;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(PORT)
                .intercept(new AuthInterceptor())
                .addService(new MetadataService())
                .build();

        server.start();
        System.out.println("Server is running in port:" + PORT);
        server.awaitTermination();
    }
}
