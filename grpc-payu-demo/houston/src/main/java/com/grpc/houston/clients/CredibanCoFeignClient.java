package com.grpc.houston.clients;

import com.grpc.core.model.PaymentResponse;
import com.grpc.core.model.Request;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "credibanco-feign-client", url = "http://localhost:8082")
public interface CredibanCoFeignClient {

     @PostMapping(value = "/credibanco-payment")
     PaymentResponse process(@RequestBody Request  request);
}
