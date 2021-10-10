package com.grpc.responsecode.service;


import com.grpc.payu.response.ResponseCodeServiceGrpc;
import org.lognet.springboot.grpc.GRpcService;

@GRpcService
public class ResponseCodeService extends ResponseCodeServiceGrpc.ResponseCodeServiceImplBase {
}
