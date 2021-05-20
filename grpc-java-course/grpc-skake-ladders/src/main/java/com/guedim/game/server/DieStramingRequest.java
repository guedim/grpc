package com.guedim.game.server;

import com.guedim.game.Die;
import com.guedim.game.GameState;
import com.guedim.game.Player;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.ThreadLocalRandom;

public class DieStramingRequest implements StreamObserver<Die> {

    private Player client;
    private Player server;
    private StreamObserver<GameState> gameStateStreamObserver;

    public DieStramingRequest(Player client, Player server, StreamObserver<GameState> gameStateStreamObserver) {
        this.client = client;
        this.server = server;
        this.gameStateStreamObserver = gameStateStreamObserver;
    }

    @Override
    public void onNext(Die die) {
        client = getNewPlayerPosition(client, die.getValue());

        if (client.getPosition() != 100) {
            server = getNewPlayerPosition(server, ThreadLocalRandom.current().nextInt(1,7));
        }

        gameStateStreamObserver.onNext(getGameState());
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        gameStateStreamObserver.onCompleted();
    }

    private GameState getGameState() {
        return GameState.newBuilder()
                .addPlayer(client)
                .addPlayer(server)
                .build();
    }


    private Player getNewPlayerPosition(Player player, int dieValue) {
        int position = player.getPosition() + dieValue;
        position = SnakeAndLaddersMap.getPosition(position);
        if(position <= 100) {
            player = player.toBuilder().setPosition(position).build();
        }
        return player;
    }
}
