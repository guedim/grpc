package com.guedim.client;

import com.guedim.model.Balance;
import com.guedim.model.BalanceCheckRequest;
import com.guedim.model.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private int PORT  = 6565;
    private String HOST = "localhost";
    private BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void  setup() {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT).usePlaintext().build();

        blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @Test
    public void balanceTest() {
        BalanceCheckRequest checkRequest = BalanceCheckRequest.newBuilder().setAccountNumber(5).build();
        Balance balance = blockingStub.getBalance(checkRequest);
        System.out.println("Received balance:" + balance.getAmount() +  " for account:" + checkRequest.getAccountNumber());
    }
}
