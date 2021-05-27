package com.guedim.server.metadata;

import com.google.common.util.concurrent.Uninterruptibles;
import com.guedim.model.*;
import com.guedim.server.rpctypes.AccountDatabase;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;
/*
    https://www.vinsguru.com/grpc-error-handling/
 */
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


    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {

        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();
        Integer balance = AccountDatabase.getBalance(accountNumber);

        for (int i = 0; i < (amount/10); i++) {

            if (balance < amount) {
                invalidAmount(responseObserver, balance, ErrorMessage.INSUFICIENT_FUNDS);
                return;
            }
            
            if (amount < 10 || (amount%10!=0) ) {
                invalidAmount(responseObserver, balance, ErrorMessage.ONLY_TEN_MULTIPLES);
                return;
            }

            Money money = Money.newBuilder()
                    .setValue(AccountDatabase.deductBalance(accountNumber,amount))
                    .build();
            responseObserver.onNext(money);
            balance = money.getValue();
        }
        responseObserver.onCompleted();
    }

    private void invalidAmount(StreamObserver<Money> responseObserver, int balance, ErrorMessage errorMessage) {

        Metadata metadata = new Metadata();
        Metadata.Key<WithdrawalError> errorKey = ProtoUtils.keyForProto(WithdrawalError.getDefaultInstance());
        WithdrawalError withDrawalError = WithdrawalError.newBuilder()
                .setBalance(balance)
                .setErrorMessage(errorMessage)
                .build();
        metadata.put(errorKey, withDrawalError);

        StatusRuntimeException status = Status.FAILED_PRECONDITION.asRuntimeException(metadata);
        responseObserver.onError(status);
    }
}