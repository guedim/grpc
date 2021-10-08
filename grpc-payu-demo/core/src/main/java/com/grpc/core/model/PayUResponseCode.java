package com.grpc.core.model;

public enum PayUResponseCode {
    APPROVED,
    PAYMENT_NETWORK_REJECTED,
    INSUFFICIENT_FUNDS,
    INVALID_CARD,
    EXPIRED_CARD,
    EXCEEDED_AMOUNT
}
