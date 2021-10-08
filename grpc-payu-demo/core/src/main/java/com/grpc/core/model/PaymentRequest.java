package com.grpc.core.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest extends Request {
    private PayUPaymentNetwork paymentNetwork;
}
