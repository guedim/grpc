package com.guedim.server.metadata;

import io.grpc.*;

import java.util.Objects;

public class AuthInterceptor implements ServerInterceptor {

    /*
        USER:ROLE
        user-secret3: prime
        user-secret2:regular
     */
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        String clientToken = headers.get(ServerConstants.USER_TOKEN);
        if(isValidClientToken(clientToken)){
            UserRole userRole = extractUserRole(clientToken);
            Context context = Context.current().withValue(ServerConstants.CTX_USER_ROLE, userRole);

            return  Contexts.interceptCall(context, call, headers, next);
            //return next.startCall(call, headers);
        } else {
            Status status = Status.UNAUTHENTICATED.withDescription("Invalid token/expired token");
            call.close(status, headers);
        }
        return new ServerCall.Listener<ReqT>() {};
    }

    private boolean isValidClientToken(String token){
        return Objects.nonNull(token) &&
                (token.startsWith("user-secret-3") || token.startsWith("user-secret-2"));
    }

    private UserRole extractUserRole(String token){
        return token.endsWith("prime") ? UserRole.PRIME : UserRole.STANDARD;
    }
}
