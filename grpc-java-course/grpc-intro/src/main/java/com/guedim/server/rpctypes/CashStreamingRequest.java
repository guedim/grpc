package com.guedim.server.rpctypes;

import com.guedim.model.Balance;
import com.guedim.model.DepositRequest;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CashStreamingRequest implements StreamObserver<DepositRequest> {

    private StreamObserver<Balance> balanceStreamObserver;
    private int accountBalance;

    public CashStreamingRequest(StreamObserver<Balance> balanceStreamObserver) {
        this.balanceStreamObserver = balanceStreamObserver;
    }

    @Override
    public void onNext(DepositRequest depositRequest) {
        int accountNumber = depositRequest.getAccountNumber();
        int amount = depositRequest.getAmount();
        System.out.println("Deposit account: " + accountNumber + " with amount: " + amount );
        this.accountBalance =  AccountDatabase.addBalance(accountNumber, amount);
    }

    @Override
    public void onError(Throwable throwable) {
        Status status = Status.INTERNAL.withDescription("No valid parameter: " + throwable.getMessage());
        balanceStreamObserver.onError(status.asRuntimeException());
    }

    @Override
    public void onCompleted() {
        Balance balance = Balance.newBuilder().setAmount(this.accountBalance).build();
        balanceStreamObserver.onNext(balance);
        balanceStreamObserver.onCompleted();
    }
}