package com.guedim.client.metadata;

import com.guedim.model.Money;
import com.guedim.model.WithdrawalError;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class MoneyStramingResponse implements StreamObserver<Money> {

    private CountDownLatch latch;

    public MoneyStramingResponse(CountDownLatch latch) {
        this.latch = latch;
    }


    @Override
    public void onNext(Money money) {
        System.out.println("Received async balance:" + money.getValue());
    }

    @Override
    public void onError(Throwable throwable) {

        Status status = Status.fromThrowable(throwable);
        Metadata metadata = Status.trailersFromThrowable(throwable);
        WithdrawalError error = metadata.get(ClientConstants.WITHDRAWAL_ERROR_KEY);

        System.out.println(throwable.getMessage());
        if (error != null) {
            System.out.println( error.getBalance() + " : " + error.getErrorMessage());
        }
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        System.out.println("Server is  done :) ");
        latch.countDown();
    }
}
