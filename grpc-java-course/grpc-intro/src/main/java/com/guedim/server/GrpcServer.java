package com.guedim.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer {

    public static int PORT = 6565;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(PORT)
                .addService(new BankService())
                .build();

        server.start();
        System.out.println("Server is running in port:" + PORT);
        server.awaitTermination();
    }
}
