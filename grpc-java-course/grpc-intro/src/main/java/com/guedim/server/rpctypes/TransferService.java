package com.guedim.server.rpctypes;

import com.guedim.model.TransferRequest;
import com.guedim.model.TransferResponse;
import com.guedim.model.TransferServiceGrpc;
import io.grpc.stub.StreamObserver;

public class TransferService extends TransferServiceGrpc.TransferServiceImplBase {

    @Override
    public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
        return new TransferStreamingRequest(responseObserver);
    }
}
