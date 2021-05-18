package com.guedim.client;

import com.google.common.util.concurrent.Uninterruptibles;
import com.guedim.model.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private int PORT  = 6565;
    private String HOST = "localhost";
    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void  setup() {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT).usePlaintext().build();

        blockingStub = BankServiceGrpc.newBlockingStub(channel);
        bankServiceStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void balanceTest() {
        BalanceCheckRequest checkRequest = BalanceCheckRequest
                .newBuilder()
                .setAccountNumber(200)
                .build();
        Balance balance = blockingStub.getBalance(checkRequest);
        System.out.println("Received balance:" + balance.getAmount() +  " for account:" + checkRequest.getAccountNumber());
    }

    // Blocking client
    @Test
    public void withdrawTest() {
        WithdrawRequest withdrawRequest  = WithdrawRequest
                .newBuilder()
                .setAccountNumber(2)
                .setAmount(500)
                .build();
        blockingStub.withdraw(withdrawRequest)
                .forEachRemaining(
                        money -> System.out.println("Received balance:" + money.getValue())
                );
    }

    // Non Blocking client
    @Test
    public void withdrawAsyncTest() {

        WithdrawRequest withdrawRequest  = WithdrawRequest
                .newBuilder()
                .setAccountNumber(2)
                .setAmount(500)
                .build();

        bankServiceStub.withdraw(withdrawRequest, new MoneyStramingResponse());
        Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
    }
}
