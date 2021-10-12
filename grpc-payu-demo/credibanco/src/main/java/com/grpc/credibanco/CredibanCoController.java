package com.grpc.credibanco;

import com.grpc.core.model.PayUResponseCode;
import com.grpc.core.model.PayUState;
import com.grpc.core.model.PaymentResponse;
import com.grpc.core.model.Request;
import com.grpc.core.util.ResponseCodeGeneratorUtil;
import com.grpc.credibanco.service.ResponseCodeClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// curl -X POST localhost:8082/credibanco-payment -H 'Content-type:application/json' -d '{"value": 100}'
@RestController
public class CredibanCoController {

    private ResponseCodeClientService service;

    @Autowired
    public CredibanCoController(ResponseCodeClientService service) {
        this.service = service;
    }

    @PostMapping(value = "/credibanco-payment")
    PaymentResponse process(@RequestBody Request request) {

        String paymentNetworkResponse = ResponseCodeGeneratorUtil.generateResponseCode();
        return service.getResponse(paymentNetworkResponse);
    }
}