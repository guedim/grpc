package com.guedim.client.metadata;

import io.grpc.CallCredentials;
import io.grpc.Metadata;

import java.util.concurrent.Executor;

public class UserSeesionToken extends CallCredentials {

    private String jwt;

    public UserSeesionToken(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public void applyRequestMetadata(RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {

        appExecutor.execute(() -> {
                    Metadata metadata = new Metadata();
                    metadata.put(ClientConstants.USER_TOKEN, jwt);
                    applier.apply(metadata);
                    //applier.fail();
                });
    }

    @Override
    public void thisUsesUnstableApi() {
        // may change in future
    }
}
