package com.grpc.houston.clients;

import com.grpc.core.model.PaymentResponse;
import com.grpc.core.model.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "visanet-feign-client", url = "http://localhost:8081")
public interface VisanetFeignClient {

     @PostMapping(value = "/visanet-payment")
     PaymentResponse process(@RequestBody Request  request);
}
