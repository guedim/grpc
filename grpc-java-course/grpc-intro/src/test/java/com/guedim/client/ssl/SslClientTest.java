package com.guedim.client.ssl;

import com.guedim.client.metadata.UserSeesionToken;
import com.guedim.model.Balance;
import com.guedim.model.BalanceCheckRequest;
import com.guedim.model.BankServiceGrpc;
import com.guedim.server.ssl.GrpcServer;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.net.ssl.SSLException;
import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SslClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;


    @BeforeAll
    public void setup() throws SSLException {

        int PORT = 6565;
        String HOST = "localhost";

        String CA_CERT = "ssl/ca.cert.pem";
        ClassLoader classLoader = GrpcServer.class.getClassLoader();
        File caCert = new File(classLoader.getResource(CA_CERT).getFile());

        SslContext sslContext = GrpcSslContexts.forClient()
                .trustManager(caCert)
                .build();

        ManagedChannel channel = NettyChannelBuilder
                .forAddress(HOST, PORT)
                .sslContext(sslContext)
                .build();
        blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void balanceTest() {
        BalanceCheckRequest checkRequest = BalanceCheckRequest.newBuilder()
                .setAccountNumber(7)
                .build();

        int random = ThreadLocalRandom.current().nextInt(1,4);
        Balance balance = blockingStub.getBalance(checkRequest);
        System.out.println("Received balance:" + balance.getAmount() + " for account:" + checkRequest.getAccountNumber());

    }
}
