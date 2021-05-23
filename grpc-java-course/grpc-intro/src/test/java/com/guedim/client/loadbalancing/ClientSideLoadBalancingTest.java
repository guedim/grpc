package com.guedim.client.loadbalancing;

import com.guedim.client.rpctypes.BalanceStreamObserver;
import com.guedim.model.Balance;
import com.guedim.model.BalanceCheckRequest;
import com.guedim.model.BankServiceGrpc;
import com.guedim.model.DepositRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.NameResolverRegistry;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientSideLoadBalancingTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;


    @BeforeAll
    public void setup() {

        String target = "bank-service";
        String balancingPolicy = "round_robin";

        List<String> instances = new ArrayList<>();
        instances.add("localhost:6565");
        instances.add("localhost:7575");

        ServiceRegistry.register(target, instances);

        NameResolverRegistry.getDefaultRegistry().register(new TempNameResolverProvider());

        ManagedChannel channel = ManagedChannelBuilder
                .forTarget(target)
                .defaultLoadBalancingPolicy(balancingPolicy)
                .usePlaintext()
                .build();
        blockingStub = BankServiceGrpc.newBlockingStub(channel);
        bankServiceStub = BankServiceGrpc.newStub(channel);
    }

    @Test
    public void balanceTest() throws InterruptedException {
        for (int i = 1; i < 100; i++) {
            BalanceCheckRequest checkRequest = BalanceCheckRequest
                    .newBuilder()
                    .setAccountNumber(ThreadLocalRandom.current().nextInt(1,11))
                    .build();
            Thread.sleep(500);
            Balance balance = blockingStub.getBalance(checkRequest);
            System.out.println("Received balance:" + balance.getAmount() + " for account:" + checkRequest.getAccountNumber());
        }
    }

    @Test
    public void cashStreamingRequest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<DepositRequest> streamObserver =  this.bankServiceStub.cashDeposit(new BalanceStreamObserver(latch));
        for (int i = 0; i < 10; i++) {
            DepositRequest request =  DepositRequest.newBuilder()
                    .setAccountNumber(8)
                    .setAmount(i*1000)
                    .build();
            streamObserver.onNext(request);
        }
        streamObserver.onCompleted();
        latch.await();
    }
}
