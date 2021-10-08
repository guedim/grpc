package com.grpc.houston;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private String paymentNetwork;
    private BigDecimal value;
}
