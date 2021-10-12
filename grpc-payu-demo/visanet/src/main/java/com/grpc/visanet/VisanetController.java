package com.grpc.visanet;

import com.grpc.core.model.*;
import com.grpc.core.util.ResponseCodeGeneratorUtil;
import com.grpc.visanet.service.ResponseCodeClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// curl -X POST localhost:8081/visanet-payment -H 'Content-type:application/json' -d '{"value": 100}'
@RestController
public class VisanetController {

    private ResponseCodeClientService service;

    @Autowired
    public VisanetController(ResponseCodeClientService service) {
        this.service = service;
    }

    @PostMapping(value = "/visanet-payment")
    PaymentResponse process(@RequestBody Request request) {

        String paymentNetworkResponse = ResponseCodeGeneratorUtil.generateResponseCode();
        return service.getResponse(paymentNetworkResponse);
    }
}