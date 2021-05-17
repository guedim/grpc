package com.guedim.server;

import com.guedim.model.Balance;
import com.guedim.model.BalanceCheckRequest;
import com.guedim.model.BankServiceGrpc;

public class BankService extends BankServiceGrpc.BankServiceImplBase {


    @Override
    public void getBalance(BalanceCheckRequest request, io.grpc.stub.StreamObserver<Balance> responseObserver) {

        int accountNumber = request.getAccountNumber();
        Balance balance = Balance.newBuilder().setAmount(accountNumber * 10).build();
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }
}
