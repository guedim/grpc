package com.grpc.visanet;

import com.grpc.core.model.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// curl -X POST localhost:8081/visanet-payment -H 'Content-type:application/json' -d '{"value": 100}'
@RestController
public class VisanetController {

    @PostMapping(value = "/visanet-payment")
    PaymentResponse process(@RequestBody Request request) {
        PaymentResponse response = new PaymentResponse();
        response.setState(PayUState.APPROVED);
        response.setResponseCode(PayUResponseCode.APPROVED);
        return response;
    }
}