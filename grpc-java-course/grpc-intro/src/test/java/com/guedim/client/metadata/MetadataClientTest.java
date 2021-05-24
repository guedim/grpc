package com.guedim.client.metadata;

import com.guedim.client.deadline.DeadlineInterceptor;
import com.guedim.model.Balance;
import com.guedim.model.BalanceCheckRequest;
import com.guedim.model.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.MetadataUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MetadataClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setup() {

        int PORT = 6565;
        String HOST = "localhost";

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(HOST, PORT)
                .intercept(MetadataUtils.newAttachHeadersInterceptor(ClientConstants.getClientToken()))
                .intercept(new DeadlineInterceptor())
                .usePlaintext()
                .build();
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
                    .getBalance(checkRequest);
            System.out.println("Received balance:" + balance.getAmount() + " for account:" + checkRequest.getAccountNumber());
        }catch (StatusRuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}
