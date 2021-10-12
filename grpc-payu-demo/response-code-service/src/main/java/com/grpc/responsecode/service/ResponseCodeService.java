package com.grpc.responsecode.service;


import com.grpc.payu.common.PayUPaymentNetwork;
import com.grpc.payu.common.PayUResponseCode;
import com.grpc.payu.common.PayUState;
import com.grpc.payu.response.ResponseCodeRequest;
import com.grpc.payu.response.ResponseCodeResponse;
import com.grpc.payu.response.ResponseCodeServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.javatuples.Pair;
import org.lognet.springboot.grpc.GRpcService;

import java.util.ArrayList;
import java.util.List;


// See: https://github.com/LogNet/grpc-spring-boot-starter
@GRpcService
public class ResponseCodeService extends ResponseCodeServiceGrpc.ResponseCodeServiceImplBase {

    @Override
    public void getResponseCode(ResponseCodeRequest request, StreamObserver<ResponseCodeResponse> responseObserver) {

        PayUPaymentNetwork payUPaymentNetwork =  request.getPayuPaymentNetwork();
        String externalResponseCode = request.getExternalPaymentNetworkResponseCode();

        // Get State and Response Code
        Pair<PayUState, PayUResponseCode> payuResponse = calculateResponseCode(externalResponseCode);

        ResponseCodeResponse response = ResponseCodeResponse.newBuilder()
                .setPayuState(payuResponse.getValue0())
                .setPayuResponseCode(payuResponse.getValue1())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private Pair<PayUState, PayUResponseCode> calculateResponseCode(String externalResponseCode) {

        // Map for response codes
        List<Pair<PayUState, PayUResponseCode>> responseCodeList = new ArrayList<>();
        responseCodeList.add(new Pair<PayUState, PayUResponseCode>(PayUState.PAYU_APPROVED, PayUResponseCode.APPROVED));
        responseCodeList.add(new Pair<PayUState, PayUResponseCode>(PayUState.PAYU_DECLINED, PayUResponseCode.PAYMENT_NETWORK_REJECTED));
        responseCodeList.add(new Pair<PayUState, PayUResponseCode>(PayUState.PAYU_DECLINED, PayUResponseCode.INSUFFICIENT_FUNDS));
        responseCodeList.add(new Pair<PayUState, PayUResponseCode>(PayUState.PAYU_DECLINED, PayUResponseCode.INVALID_CARD));
        responseCodeList.add(new Pair<PayUState, PayUResponseCode>(PayUState.PAYU_DECLINED, PayUResponseCode.EXPIRED_CARD));
        responseCodeList.add(new Pair<PayUState, PayUResponseCode>(PayUState.PAYU_DECLINED, PayUResponseCode.EXCEEDED_AMOUNT));
        responseCodeList.add(new Pair<PayUState, PayUResponseCode>(PayUState.PAYU_DECLINED, PayUResponseCode.INVALID_EXPIRATION_DATE_OR_SECURITY_CODE));
        responseCodeList.add(new Pair<PayUState, PayUResponseCode>(PayUState.PAYU_DECLINED, PayUResponseCode.CREDIT_CARD_NOT_AUTHORIZED_FOR_INTERNET_TRANSACTIONS));
        responseCodeList.add(new Pair<PayUState, PayUResponseCode>(PayUState.PAYU_DECLINED, PayUResponseCode.PAYMENT_NETWORK_NO_CONNECTION));
        responseCodeList.add(new Pair<PayUState, PayUResponseCode>(PayUState.PAYU_DECLINED, PayUResponseCode.PAYMENT_NETWORK_NO_RESPONSE));
        responseCodeList.add(new Pair<PayUState, PayUResponseCode>(PayUState.PAYU_DECLINED, PayUResponseCode.PAYMENT_NETWORK_BAD_RESPONSE));


        int responseCode = Integer.valueOf(externalResponseCode).intValue();
        responseCode = responseCode / 10;

        return responseCodeList.get(responseCode);
    }
}
