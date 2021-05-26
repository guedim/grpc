package com.guedim.server.metadata;

import com.google.common.util.concurrent.Uninterruptibles;
import com.guedim.model.Balance;
import com.guedim.model.BalanceCheckRequest;
import com.guedim.model.BankServiceGrpc;
import com.guedim.model.Money;
import com.guedim.server.rpctypes.AccountDatabase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class MetadataService extends BankServiceGrpc.BankServiceImplBase {


    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {

        int accountNumber = request.getAccountNumber();
        int amount = AccountDatabase.getBalance(accountNumber);


        UserRole userRole = ServerConstants.CTX_USER_ROLE.get();
        UserRole userRole1 = ServerConstants.CTX_USER_ROLE1.get();
        amount = UserRole.PRIME.equals(userRole) ? amount : amount -5;

        System.out.println(userRole + " : " + userRole1);

        Balance balance = Balance.newBuilder()
                .setAmount(amount)
                .build();

        // Simulate a time consuming
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    private void invalidAmount(StreamObserver<Money> responseObserver, int accountNumber, Integer balance) {
        Status status = Status.FAILED_PRECONDITION.withDescription("No enough money. Account:" + accountNumber + " only have USD $" + balance);
        responseObserver.onError(status.asRuntimeException());
    }
}