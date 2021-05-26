package com.guedim.server.metadata;

import io.grpc.*;

import java.util.Objects;

public class AuthInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        String clientToken = headers.get(ServerConstants.USER_TOKEN);
        if(isValidClientToken(clientToken)){
            return next.startCall(call, headers);
        } else {
            Status status = Status.UNAUTHENTICATED.withDescription("Invalid token/expired token");
            call.close(status, headers);
        }
        return new ServerCall.Listener<ReqT>() {};
    }

    private boolean isValidClientToken(String token){
        return Objects.nonNull(token) && token.equals("user-secret-3");
    }
}
