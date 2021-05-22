package com.guedim.game.server;

import com.guedim.game.Die;
import com.guedim.game.GameServiceGrpc;
import com.guedim.game.GameState;
import com.guedim.game.Player;
import io.grpc.stub.StreamObserver;

public class GameService extends GameServiceGrpc.GameServiceImplBase {

    @Override
    public StreamObserver<Die> roll(StreamObserver<GameState> responseObserver) {

        Player client = Player.newBuilder()
                .setName("client")
                .setPosition(0)
                .build();
        Player server = Player.newBuilder()
                .setName("server")
                .setPosition(0)
                .build();
        return new DieStramingRequest(client, server, responseObserver);
    }
}
