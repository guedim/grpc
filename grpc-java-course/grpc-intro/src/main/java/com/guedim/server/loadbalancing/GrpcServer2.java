package com.guedim.server.loadbalancing;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GrpcServer2 {

    public static int PORT = 7575;

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(PORT)
                .addService(new BankService())
                .build();

        server.start();
        System.out.println("Server 2 is running in port:" + PORT);
        server.awaitTermination();
    }
}
