package com.burnyarosh.processor;

import com.burnyarosh.dto.Success;
import com.burnyarosh.entity.Entity;
import com.burnyarosh.entity.Game;
import com.burnyarosh.entity.Player;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.burnyarosh.processor.EventBusAddress.*;

public class GameLobbyVerticle extends AbstractVerticle {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameLobbyVerticle.class);
    private static final String ERROR_PLAYER_NAME_EXISTS = "Conflict: Username already taken";
    private static final String ERROR_NO_PLAYER_EXISTS = "Error: Player does not exist";
    private static final String ERROR_MALFORMED_REQUEST = "Malformed request";
    private static final String ERROR_MALFORMED_REQUEST_TEMPLATE = "Malformed request: %s";

    private static final String GAME_GUID = "gameGUID";
    private static final String PLAYER_GUID = "playerGUID";

    List<Player> activePlayers = new ArrayList();
    List<Game> activeGames = new ArrayList<>();
    private Map<String, Game> activeGamesById = new HashMap<>();
    private Map<String, Player> activePlayersById = new HashMap<>();
    private Map<String, Player> activePlayersByName = new HashMap<>();
    private Map<String, String> verticleDeploymentByGUID = new HashMap<>();

    @Override
    public void start(Future<Void> future) throws Exception {
        MessageConsumer<JsonObject> newPlayer = vertx.eventBus().consumer(NEW_PLAYER_ADDRESS.getAddress());
        newPlayer.handler(this::newPlayer);

        MessageConsumer<JsonObject> newLobby = vertx.eventBus().consumer(NEW_LOBBY_ADDRESS.getAddress());
        newLobby.handler(this::newLobby);

        MessageConsumer<JsonObject> joinLobby = vertx.eventBus().consumer(JOIN_LOBBY_ADDRESS.getAddress());
        joinLobby.handler(this::joinLobby);

        MessageConsumer<Void> listLobbies = vertx.eventBus().consumer(LIST_LOBBY_ADDRESS.getAddress());
        listLobbies.handler(this::listLobbies);

        MessageConsumer<Void> listPlayers = vertx.eventBus().consumer(LIST_PLAYER_ADDRESS.getAddress());
        listPlayers.handler(this::listPlayers);

        LOGGER.info("Successfully deployed GameLobbyVerticle");
    }

    private void newPlayer(Message<JsonObject> message) {
        JsonObject body = message.body();
        if (!body.containsKey("username")) message.fail(400, ERROR_MALFORMED_REQUEST);
        for (Player p : activePlayers) {
            if (body.getString("username").equals(p.getName())) message.fail(409, ERROR_PLAYER_NAME_EXISTS);
        }
        Player temp = new Player(body.getString("username"), Player.generateGUID());
        this.activePlayers.add(temp);
        message.reply(new Success().put("playerGUID", temp.getId()));
    }

    private void listPlayers(Message<Void> message) {
        JsonArray result = new JsonArray();
        this.activePlayers.forEach(player -> {
            result.add(player.getName());
        });
        message.reply(new Success().put("players", result));
    }

    private void listLobbies(Message<Void> message) {
        JsonArray result = new JsonArray();
        this.activeGamesById.keySet().forEach(key -> {
            result.add(key);
        });
        message.reply(new Success().put("lobbies", result));
    }

    private JsonObject joinLobby(Message<JsonObject> request) {
        return null;
    }

    /**
     * Message should have param guid
     * TODO: is not currently part of existing lobby.
     */
    private void newLobby(Message<JsonObject> request) {
        JsonObject req = request.body();
        String guid = req.getString(PLAYER_GUID);
        if (guid == null)
            request.fail(400, String.format(ERROR_MALFORMED_REQUEST_TEMPLATE, "Missing guid from request"));
        Player player = activePlayersById.get(guid);
        if (player == null) request.fail(400, ERROR_NO_PLAYER_EXISTS);

        String lobbyguid = Entity.generateGUID();
        Game game = new Game(lobbyguid, lobbyguid, player);
        activeGamesById.put(lobbyguid, game);
        activeGames.add(game);

        JsonObject config = new JsonObject();
        config.put(GAME_GUID, lobbyguid);
        config.put(PLAYER_GUID, player.getId());

        super.vertx.deployVerticle(GameVerticle.class.getName(), ar -> {
            if (ar.succeeded()) {
                verticleDeploymentByGUID.put(guid, ar.result());
                LOGGER.info(String.format("Deployed verticle %s for game %s", ar.result(), guid));
                request.reply(new Success().put(GAME_GUID, guid));
            } else {

            }
        });
    }
}
