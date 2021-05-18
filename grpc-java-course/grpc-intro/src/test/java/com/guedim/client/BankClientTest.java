package com.guedim.client;

import com.guedim.model.Balance;
import com.guedim.model.BalanceCheckRequest;
import com.guedim.model.BankServiceGrpc;
import com.guedim.model.WithdrawRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void  setup() {

        int PORT = 6565;
        String HOST = "localhost";

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
    public void withdrawAsyncTest() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        WithdrawRequest withdrawRequest  = WithdrawRequest
                .newBuilder()
                .setAccountNumber(2)
                .setAmount(500)
                .build();

        bankServiceStub.withdraw(withdrawRequest, new MoneyStramingResponse(latch));
        latch.await();
    }
}
