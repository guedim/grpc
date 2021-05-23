package com.guedim.server.deadline;

import com.google.common.util.concurrent.Uninterruptibles;
import com.guedim.model.*;
import com.guedim.server.rpctypes.AccountDatabase;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class DeadlineService extends BankServiceGrpc.BankServiceImplBase {


    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {

        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder()
                .setAmount(AccountDatabase.getBalance(accountNumber))
                .build();

        // Simulate a time consuming
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {

        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();
        Integer balance = AccountDatabase.getBalance(accountNumber);

        for (int i = 0; i < (amount/2); i++) {

            if (balance < amount) {
                invalidAmount(responseObserver,accountNumber, balance);
                return;
            }

            Money money = Money.newBuilder()
                    .setValue(AccountDatabase.deductBalance(accountNumber,amount))
                    .build();
            Uninterruptibles.sleepUninterruptibly(3,TimeUnit.SECONDS);

            // check if client is already listening
            if(!Context.current().isCancelled()){
                responseObserver.onNext(money);
                System.out.println("Delivering: " + amount + " for account:" + accountNumber);
                balance = money.getValue();
            } else {
                break;
            }
        }
        System.out.println("withdraw request  completed");
        responseObserver.onCompleted();
    }

    private void invalidAmount(StreamObserver<Money> responseObserver, int accountNumber, Integer balance) {
        Status status = Status.FAILED_PRECONDITION.withDescription("No enough money. Account:" + accountNumber + " only have USD $" + balance);
        responseObserver.onError(status.asRuntimeException());
    }
}