package com.burnyarosh.api.processor;

import com.burnyarosh.api.dto.common.CoordDTO;
import com.burnyarosh.api.dto.in.PlayerTurnDTO;
import com.burnyarosh.api.dto.internal.PlayerUpdateDTO;
import com.burnyarosh.board.Chess;
import com.burnyarosh.api.dto.out.SuccessDTO;
import com.burnyarosh.api.dto.entity.Player;
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
    private Chess chess;
    private int whitePlayer; // which player is white. corresponds to array index.
    private MessageConsumer<JsonObject> entryPoint;
    private MessageConsumer<PlayerTurnDTO> playerTurn;

    @Override
    public void start(Promise<Void> promise) {
        JsonObject config = this.config();
        this.players = new Player[2];

        // Message Consumer Entry points
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
                this.chess.play(origin.getX(), origin.getY(), target.getX(), target.getY());
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
        this.randomColor();
        this.sendGameUpdate();
        // unregisters consumer so that no more players can be added. Think about adding possibility for spectators.
        playerTurn.unregister();
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
        JsonObject chessJson = this.chess.toJson();
        for (Player p : players) {
            JsonObject update = new JsonObject();
            update.mergeIn(chessJson);
            update.put("turn", this.isPlayerTurn(p));
            update.put("blackPlayer", getPlayerUsername(false));
            update.put("whitePlayer", getPlayerUsername(true));
            update.put("player", p.getId());
            this.sendPlayerGameUpdate(p, update);
        }
    }

    private void sendPlayerGameUpdate(Player p, JsonObject update) {
        PlayerUpdateDTO dto = new PlayerUpdateDTO();
        dto.setJson(update);
        dto.setPlayerGUID(p.getId());
        super.vertx.eventBus().publish(UPDATE_PLAYERS_ADDRESS.getAddressString(), dto);
    }

    private void newGame() {
        this.chess = new Chess();
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

    private String getPlayerUsername(boolean white) {
        if (white) {
            return this.players[this.whitePlayer].getName();
        } else {
            return this.players[this.whitePlayer == 0 ? 1 : 0].getName();
        }
    }

    private boolean isPlayerTurn(Player p) {
        for (int ii = 0; ii < this.players.length; ii++) {
            if( players[ii].equals(p)) {
                return isPlayerTurn(ii);
            }
        }
        return false;
    }

    private boolean isPlayerTurn(int player) {
        return this.whitePlayer == player && this.chess.getTurn() == Chess.Color.WHITE
                || this.whitePlayer != player && this.chess.getTurn() == Chess.Color.BLACK;
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
