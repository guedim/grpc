package com.guedim.client.deadline;

import com.guedim.model.Balance;
import com.guedim.model.BalanceCheckRequest;
import com.guedim.model.BankServiceGrpc;
import com.guedim.model.WithdrawRequest;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DeadlineClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {

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
                .setAccountNumber(7)
                .build();
        try {
            Balance balance = blockingStub
                    .withDeadline(Deadline.after(2, TimeUnit.SECONDS))
                    .getBalance(checkRequest);
            System.out.println("Received balance:" + balance.getAmount() + " for account:" + checkRequest.getAccountNumber());
        }catch (StatusRuntimeException e) {
            // go with default value
            System.err.println(e.getMessage());
        }
    }

    // Blocking client
    @Test
    public void withdrawTest() {
        WithdrawRequest withdrawRequest  = WithdrawRequest
                .newBuilder()
                .setAccountNumber(6)
                .setAmount(10)
                .build();

        try {
            blockingStub
                    .withDeadline(Deadline.after(10, TimeUnit.SECONDS))
                    .withdraw(withdrawRequest)
                    .forEachRemaining(
                            money -> System.out.println("Received balance:" + money.getValue())
                    );
        } catch (StatusRuntimeException e) {
            // go with default value
            System.err.println(e.getMessage());
        }
    }
}
