package com.guedim.client.rpctypes;

import com.guedim.model.BankServiceGrpc;
import com.guedim.model.TransferServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;

public abstract  class  AbstractClientTest {

    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;
    protected BankServiceGrpc.BankServiceStub bankServiceStub;

    protected TransferServiceGrpc.TransferServiceStub transferServiceStub;

    @BeforeAll
    public void  setup() {

        int PORT = 6565;
        String HOST = "localhost";

        ManagedChannel channel = ManagedChannelBuilder.forAddress(HOST, PORT).usePlaintext().build();

        blockingStub = BankServiceGrpc.newBlockingStub(channel);
        bankServiceStub = BankServiceGrpc.newStub(channel);

        transferServiceStub = TransferServiceGrpc.newStub(channel);
    }
}
