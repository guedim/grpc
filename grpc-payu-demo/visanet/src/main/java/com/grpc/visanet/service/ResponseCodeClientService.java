package com.grpc.visanet.service;

import com.grpc.core.model.PayUResponseCode;
import com.grpc.core.model.PayUState;
import com.grpc.core.model.PaymentResponse;
import com.grpc.payu.common.PayUPaymentNetwork;
import com.grpc.payu.response.ResponseCodeRequest;
import com.grpc.payu.response.ResponseCodeResponse;
import com.grpc.payu.response.ResponseCodeServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class ResponseCodeClientService {

    @GrpcClient("response-code-service")
    private ResponseCodeServiceGrpc.ResponseCodeServiceBlockingStub stub;

    public PaymentResponse getResponse(String paymentNetworkResponse) {

        ResponseCodeRequest request = ResponseCodeRequest.newBuilder()
                .setPayuPaymentNetworkValue(PayUPaymentNetwork.VISANET_VALUE)
                .setExternalPaymentNetworkResponseCode(paymentNetworkResponse)
                .build();

        ResponseCodeResponse grpcResponse =  stub.getResponseCode(request);

        // PayU Response
        PaymentResponse response = new PaymentResponse();
        response.setState(PayUState.valueOf(grpcResponse.getPayuState().name()));
        response.setResponseCode(PayUResponseCode.valueOf(grpcResponse.getPayuResponseCode().name()));
        return response;
    }
}
