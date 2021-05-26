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

import java.util.concurrent.ThreadLocalRandom;

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

        for (int i = 0; i < 20; i++) {
            try {
                int random = ThreadLocalRandom.current().nextInt(1,4);
                Balance balance = blockingStub
                        .withCallCredentials(new UserSeesionToken("user-secret-"+ random + ":standard"))
                        .getBalance(checkRequest);
                System.out.println("Received balance:" + balance.getAmount() + " for account:" + checkRequest.getAccountNumber());
            }catch (StatusRuntimeException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
