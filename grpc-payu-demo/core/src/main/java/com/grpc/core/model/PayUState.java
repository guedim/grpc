package com.grpc.core.model;

public enum PayUState {
    APPROVED,
    DECLINED;

    public static PayUState toPayUState(String payuValue) {

        try {
            return PayUState.valueOf(payuValue.substring( payuValue.indexOf("_")+1));
        } catch (Exception e) {
            return PayUState.DECLINED;
        }
    }
}
