package com.guedim.server.ssl;

import com.guedim.server.rpctypes.BankService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;

import java.io.File;
import java.io.IOException;

public class GrpcServer {

    private static final int PORT = 6565;
    private static final String LOCAL_HOST_CRT = "ssl/localhost.crt";
    private static final String LOCAL_HOST_KEY = "ssl/localhost.pem";

    public static void main(String[] args) throws IOException, InterruptedException {

        ClassLoader classLoader = GrpcServer.class.getClassLoader();
        File crt = new File(classLoader.getResource(LOCAL_HOST_CRT).getFile());
        File key = new File(classLoader.getResource(LOCAL_HOST_KEY).getFile());

        SslContext sslContext = GrpcSslContexts.configure(SslContextBuilder.forServer(crt, key)).build();

        Server server = NettyServerBuilder.forPort(PORT)
                .sslContext(sslContext)
                .addService(new BankService())
                .build();

        server.start();
        System.out.println("Server is running in port:" + PORT);
        server.awaitTermination();
    }
}
