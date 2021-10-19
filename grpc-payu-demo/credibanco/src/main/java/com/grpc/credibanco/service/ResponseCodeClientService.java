package com.grpc.credibanco.service;

import com.grpc.core.model.PayUResponseCode;
import com.grpc.core.model.PayUState;
import com.grpc.core.model.PaymentResponse;
import com.grpc.payu.common.PayUPaymentNetwork;
import com.grpc.payu.response.ResponseCodeRequest;
import com.grpc.payu.response.ResponseCodeResponse;
import com.grpc.payu.response.ResponseCodeServiceGrpc;
import io.grpc.ClientInterceptor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseCodeClientService {

    @GrpcClient("response-code-service")
    private ResponseCodeServiceGrpc.ResponseCodeServiceBlockingStub stub;

    private final List<ClientInterceptor> clientInterceptorList;

    public ResponseCodeClientService(List<ClientInterceptor> clientInterceptorList) {
        this.clientInterceptorList = clientInterceptorList;
    }

    public PaymentResponse getResponse(String paymentNetworkResponse) {

        ResponseCodeRequest request = ResponseCodeRequest.newBuilder()
                .setPayuPaymentNetwork(PayUPaymentNetwork.CREDIBANCO)
                .setExternalPaymentNetworkResponseCode(paymentNetworkResponse)
                .build();

        ResponseCodeResponse grpcResponse =  stub.withInterceptors(clientInterceptorList.toArray(ClientInterceptor[]::new)).getResponseCode(request);

        // PayU Response
        PaymentResponse response = new PaymentResponse();
        response.setState(PayUState.toPayUState(grpcResponse.getPayuState().name()));
        response.setResponseCode(PayUResponseCode.valueOf(grpcResponse.getPayuResponseCode().name()));
        return response;
    }
}
