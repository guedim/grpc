package com.grpc.houston;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// curl -X POST localhost:8081/payment -H 'Content-type:application/json' -d '{"paymentNetwork": "VISANET", "value": 100}'
@RestController
public class HoustonController {

    @PostMapping(value = "/payment")
    PaymentResponse process(@RequestBody PaymentRequest request) {
        PaymentResponse response = new PaymentResponse();
        response.setPayuResponse("aprobado");
        response.setPaymentNetworkResponse("55D");
        return response;
    }
}
