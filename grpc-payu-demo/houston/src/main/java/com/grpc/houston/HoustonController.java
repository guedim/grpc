package com.grpc.houston;

import com.grpc.core.model.*;
import com.grpc.houston.clients.CredibanCoFeignClient;
import com.grpc.houston.clients.VisanetFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// curl -X POST localhost:8080/payment -H 'Content-type:application/json' -d '{"paymentNetwork": "VISANET", "value": 100}'
@RestController
public class HoustonController {

    private VisanetFeignClient visanetClient;
    private CredibanCoFeignClient credibancoClient;

    @Autowired
    public HoustonController(VisanetFeignClient visanetClient, CredibanCoFeignClient credibancoClient) {
        this.visanetClient = visanetClient;
        this.credibancoClient = credibancoClient;
    }

    @PostMapping(value = "/payment")
    PaymentResponse process(@RequestBody PaymentRequest request) {

        if(PayUPaymentNetwork.CREDIBANCO.equals(request.getPaymentNetwork())) {
            return credibancoClient.process(request);
        } else {
            return visanetClient.process(request);
        }
    }
}
