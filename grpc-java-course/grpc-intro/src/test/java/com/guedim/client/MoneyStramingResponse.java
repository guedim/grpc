package com.guedim.client;

import com.guedim.model.Money;
import io.grpc.stub.StreamObserver;

public class MoneyStramingResponse implements StreamObserver<Money> {
    @Override
    public void onNext(Money money) {
        System.out.println("Received async balance:" + money.getValue());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        System.out.println("Server is  done :) ");
    }
}
