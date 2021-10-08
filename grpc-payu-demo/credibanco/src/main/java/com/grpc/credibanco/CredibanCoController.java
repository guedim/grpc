package com.grpc.credibanco;

import com.grpc.core.model.PayUResponseCode;
import com.grpc.core.model.PayUState;
import com.grpc.core.model.PaymentResponse;
import com.grpc.core.model.Request;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// curl -X POST localhost:8082/credibanco-payment -H 'Content-type:application/json' -d '{"value": 100}'
@RestController
public class CredibanCoController {

    @PostMapping(value = "/credibanco-payment")
    PaymentResponse process(@RequestBody Request request) {
        PaymentResponse response = new PaymentResponse();
        response.setState(PayUState.APPROVED);
        response.setResponseCode(PayUResponseCode.APPROVED);
        return response;
    }
}