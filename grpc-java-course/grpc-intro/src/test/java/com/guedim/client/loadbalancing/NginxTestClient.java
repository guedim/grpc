package com.guedim.client.loadbalancing;

import com.guedim.client.rpctypes.AbstractClientTest;
import com.guedim.model.Balance;
import com.guedim.model.BalanceCheckRequest;
import com.guedim.model.BankServiceGrpc;
import com.guedim.model.TransferServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NginxTestClient {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;


    @BeforeAll
    public void setup() {

        int PORT = 8585;
        String HOST = "localhost";

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT).usePlaintext().build();
        blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void balanceTest() throws InterruptedException {
        for (int i = 1; i < 500; i++) {
            BalanceCheckRequest checkRequest = BalanceCheckRequest
                    .newBuilder()
                    .setAccountNumber(ThreadLocalRandom.current().nextInt(1,11))
                    .build();
            Thread.sleep(500);
            Balance balance = blockingStub.getBalance(checkRequest);
            System.out.println("Received balance:" + balance.getAmount() + " for account:" + checkRequest.getAccountNumber());
        }
    }
}
