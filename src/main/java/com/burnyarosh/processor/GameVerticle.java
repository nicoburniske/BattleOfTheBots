package com.burnyarosh.processor;

import com.burnyarosh.board.ChessBoard;
import com.burnyarosh.entity.Player;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;

import java.util.Random;

import static com.burnyarosh.processor.EventBusAddress.LOBBY_BASE_ADDRESS;

public class GameVerticle extends AbstractVerticle {
    private String lobbyID;
    private Player[] players = new Player[2];
    private ChessBoard board;
    private int whitePlayer; // which player is white. corresponds to array index.

    @Override
    public void start(Promise<Void> promise) throws Exception {
        JsonObject config = this.config();
        MessageConsumer<JsonObject> entryPoint = vertx.eventBus().consumer(String.format(LOBBY_BASE_ADDRESS.getAddress(), super.deploymentID()));
        entryPoint.handler(this::handleInput);
        promise.complete();
    }

    private void handleInput(Message<JsonObject> message) {
        message.reply("URGAY");
    }

    private void newGame() {
        this.board = new ChessBoard();
    }

    public void randomColor() {
        this.whitePlayer = new Random().nextInt(2);
    }

    /**
     * @param whitePlayer should be 1 or 2.
     */
    public void setWhitePlayer(int whitePlayer) {
        this.whitePlayer = whitePlayer - 1;
    }
}
