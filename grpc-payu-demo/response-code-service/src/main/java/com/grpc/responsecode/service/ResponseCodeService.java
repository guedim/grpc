package com.grpc.responsecode.service;


import com.grpc.payu.common.PayUPaymentNetwork;
import com.grpc.payu.common.PayUResponseCode;
import com.grpc.payu.common.PayUState;
import com.grpc.payu.response.ResponseCodeRequest;
import com.grpc.payu.response.ResponseCodeResponse;
import com.grpc.payu.response.ResponseCodeServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;


// See: https://github.com/LogNet/grpc-spring-boot-starter
@GRpcService
public class ResponseCodeService extends ResponseCodeServiceGrpc.ResponseCodeServiceImplBase {

    @Override
    public void getResponseCode(ResponseCodeRequest request, StreamObserver<ResponseCodeResponse> responseObserver) {

        PayUPaymentNetwork payUPaymentNetwork =  request.getPayuPaymentNetwork();
        String externalResponseCode = request.getExternalPaymentNetworkResponseCode();

        //TODO: Generar respuesta aleatoria dependiente de la red
        ResponseCodeResponse response = ResponseCodeResponse.newBuilder()
                .setPayuResponseCode(PayUResponseCode.EXCEEDED_AMOUNT)
                .setPayuState(PayUState.DECLINED)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
