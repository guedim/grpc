package com.guedim.server;

import com.guedim.model.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {


    @Override
    public void getBalance(BalanceCheckRequest request, io.grpc.stub.StreamObserver<Balance> responseObserver) {

        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder()
                .setAmount(AccountDatabase.getBalance(accountNumber))
                .build();
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {

        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();
        Integer balance = AccountDatabase.getBalance(accountNumber);

        for (int i = 0; i < (amount/10); i++) {

            if (balance < amount) {
                invalidAmount(responseObserver,accountNumber, balance);
                return;
            }

            Money money = Money.newBuilder()
                    .setValue(AccountDatabase.deductBalance(accountNumber,amount))
                    .build();
            responseObserver.onNext(money);
            balance = money.getValue();
            sleep();
        }
        responseObserver.onCompleted();
    }

    private void invalidAmount(StreamObserver<Money> responseObserver, int accountNumber, Integer balance) {
        Status status = Status.FAILED_PRECONDITION.withDescription("No enough money. Account:" + accountNumber + " only have USD $" + balance);
        responseObserver.onError(status.asRuntimeException());
    }

    private void sleep() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}