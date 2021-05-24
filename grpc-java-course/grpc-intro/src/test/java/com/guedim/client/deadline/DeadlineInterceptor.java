package com.guedim.client.deadline;

import io.grpc.*;
import org.w3c.dom.html.HTMLImageElement;

import java.sql.Time;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DeadlineInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method, CallOptions callOptions, Channel next) {

        // Global interceptor
        Deadline deadline = callOptions.getDeadline();
        if(Objects.isNull(deadline)){
            callOptions=  callOptions.withDeadline(Deadline.after(4 , TimeUnit.SECONDS));
        }
        return next.newCall(method, callOptions);
    }
}
