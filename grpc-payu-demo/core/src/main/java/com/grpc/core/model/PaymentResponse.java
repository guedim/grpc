package com.grpc.core.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentResponse {
    private PayUState state;
    private PayUResponseCode responseCode;
}
