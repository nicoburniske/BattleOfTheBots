package com.burnyarosh.api.processor;

import com.burnyarosh.api.dto.common.CoordDTO;
import com.burnyarosh.api.dto.in.PlayerTurnDTO;
import com.burnyarosh.board.ChessBoard;
import com.burnyarosh.api.dto.out.SuccessDTO;
import com.burnyarosh.entity.Player;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.Random;

import static com.burnyarosh.api.processor.utils.Constants.GAME_GUID;
import static com.burnyarosh.api.processor.utils.EventBusAddress.*;

public class GameVerticle extends AbstractVerticle {
    private static final String ERROR_PLAYER_NOT_IN_GAME = "Error: Player not in game";
    private String lobbyID;
    private Player[] players;
    private ChessBoard board;
    private int whitePlayer; // which player is white. corresponds to array index.
    MessageConsumer<JsonObject> entryPoint;
    MessageConsumer<PlayerTurnDTO> playerTurn;

    @Override
    public void start(Promise<Void> promise) {
        JsonObject config = this.config();
        this.players = new Player[2];

        entryPoint = vertx.eventBus().consumer(String.format(LOBBY_BASE_ADDRESS.getAddressString(), super.deploymentID()));
        entryPoint.handler(this::handleSetup);
        playerTurn = vertx.eventBus().consumer((String.format(NEW_MOVE_ADDRESS.getAddressString(), super.deploymentID())));
        playerTurn.handler(this::handleNewMove);
        promise.complete();
    }

    private void handleNewMove(Message<PlayerTurnDTO> message) {
        PlayerTurnDTO dto = message.body();
        int player = this.getPlayerIndex(dto.getPlayerGUID());
        if (player < 0) message.fail(400, ERROR_PLAYER_NOT_IN_GAME);
        if (this.isPlayerTurn(player)) {
            try {
                CoordDTO origin = dto.getOrigin();
                CoordDTO target = dto.getTarget();
                this.board.playGame(origin.getX(), origin.getY(), target.getX(), target.getY());
                message.reply(new SuccessDTO());
                this.sendGameUpdate();
            } catch (Exception e) {
                message.fail(400, e.getMessage());
            }
        } else {
            //TODO: error message saying it is not the player's turn
        }
    }

    private void handleSetup(Message<JsonObject> message) {
        JsonObject messageJSON = message.body();
        String type = jsonGetStringValue(messageJSON, "type");
        this.lobbyID = jsonGetStringValue(messageJSON, GAME_GUID);
        JsonArray players = jsonGetArrayValue(messageJSON, "players");
        JsonObject player1 = players.getJsonObject(0);
        this.players[0] = new Player(player1.getString("username"), player1.getString("id"));
        JsonObject player2 = players.getJsonObject(1);
        this.players[1] = new Player(player2.getString("username"), player2.getString("id"));
        this.newGame();
        message.reply(new SuccessDTO());
        this.sendGameUpdate();
        this.randomColor();
        // TODO: make sure no more players can join. remove consumer?
    }

    private boolean isPlayerTurn(int player) {
        return this.whitePlayer == player && this.board.isWhiteTurn()
                || this.whitePlayer != player && !this.board.isWhiteTurn();
    }

    private int getPlayerIndex(String playerid) {
        if (this.players[0].getId().equals(playerid)) {
            return 0;
        } else if (this.players[1].getId().equals(playerid)) {
            return 1;
        } else {
            return -1;
        }
    }

    private void sendGameUpdate() {
        JsonObject update = new JsonObject();
        update.put("state", this.board.toJson());
        JsonArray players = new JsonArray();
        players.add(this.players[0].getId());
        players.add(this.players[1].getId());
        update.put("players", players);
        super.vertx.eventBus().publish(UPDATE_PLAYERS_ADDRESS.getAddressString(), update);
    }

    private void newGame() {
        this.board = new ChessBoard();
    }

    private void randomColor() {
        this.whitePlayer = new Random().nextInt(2);
    }

    /**
     * @param whitePlayer should be 1 or 2.
     */
    private void setWhitePlayer(int whitePlayer) {
        this.whitePlayer = whitePlayer - 1;
    }

    private String jsonGetStringValue(JsonObject json, String key) {
        Objects.requireNonNull(key);
        String value = json.getString(key);
        if (value == null) throw new IllegalArgumentException("Given key does not have a value");
        return value;
    }

    private JsonArray jsonGetArrayValue(JsonObject json, String key) {
        Objects.requireNonNull(key);
        JsonArray value = json.getJsonArray(key);
        if (value == null) throw new IllegalArgumentException("Given key does not have a value");
        return value;
    }
}
