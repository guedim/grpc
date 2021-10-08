package com.grpc.houston;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentResponse {
    private String payuResponse;
    private String paymentNetworkResponse;
}
