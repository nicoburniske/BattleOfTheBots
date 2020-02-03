package com.burnyarosh.processor;

import com.burnyarosh.dto.Success;
import com.burnyarosh.entity.Entity;
import com.burnyarosh.entity.Game;
import com.burnyarosh.entity.Player;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
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

    static final String GAME_GUID = "gameGUID";
    static final String PLAYER_GUID = "playerGUID";
    static final String PLAYER_NAME = "playerNAME";

    List<Player> activePlayers = new ArrayList();
    List<Game> activeGames = new ArrayList<>();
    private Map<String, Game> activeGamesById = new HashMap<>();
    private Map<String, Player> activePlayersById = new HashMap<>();
    private Map<String, Player> activePlayersByName = new HashMap<>();
    private Map<String, String> verticleDeploymentByGUID = new HashMap<>();

    @Override
    public void start(Promise<Void> promise) throws Exception {
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
        promise.complete();
    }

    private void newPlayer(Message<JsonObject> message) {
        JsonObject body = message.body();
        if (!body.containsKey("username")) message.fail(400, ERROR_MALFORMED_REQUEST);
        for (Player p : activePlayers) {
            if (body.getString("username").equals(p.getName())) message.fail(409, ERROR_PLAYER_NAME_EXISTS);
        }
        Player temp = new Player(body.getString("username"), Player.generateGUID());
        this.activePlayers.add(temp);
        this.activePlayersById.put(temp.getId(), temp);
        message.reply(new Success().put(PLAYER_GUID, temp.getId()));
    }

    private void listPlayers(Message<Void> message) {
        JsonArray body = new JsonArray();
        this.activePlayers.forEach(player -> {
            body.add(player.getName());
        });
        message.reply(new Success().put("players", body));
    }

    private void listLobbies(Message<Void> message) {
        JsonArray result = new JsonArray();
        this.activeGames.stream().filter(game -> !game.gameIsReady()).forEach(game -> {
            result.add(game.toJson());
        });
        message.reply(new Success().put("lobbies", result));
    }

    private void joinLobby(Message<JsonObject> message) {
        JsonObject body = message.body();
        String lobbyGUID = body.getString(GAME_GUID);
        String playerGUID = body.getString(PLAYER_GUID);
        if (lobbyGUID == null || playerGUID == null) message.fail(400, ERROR_MALFORMED_REQUEST);
        //TODO: add logic here. is lobby full? how much info do you actually need in gameVerticle? do we need player info? yes probably.
        if (activeGamesById.containsKey(lobbyGUID) && activePlayersById.containsKey(playerGUID)) {
            super.vertx.eventBus().request(String.format(LOBBY_BASE_ADDRESS.getAddress(), verticleDeploymentByGUID.get(lobbyGUID)), new JsonObject(),
                    ar -> {
               if (ar.succeeded()) {
                   activeGamesById.get(lobbyGUID).addSecondPlayer(activePlayersById.get(playerGUID));
                   message.reply(new Success());
               } else {
                   message.fail(400, ar.cause().toString());
               }
            });
        }
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
        Player player = this.activePlayersById.get(guid);
        if (player == null) request.fail(400, ERROR_NO_PLAYER_EXISTS);

        String lobbyguid = Entity.generateGUID();
        Game game = new Game(lobbyguid, lobbyguid, player);
        this.activeGamesById.put(lobbyguid, game);
        this.activeGames.add(game);

        JsonObject config = new JsonObject();
        config.put(GAME_GUID, lobbyguid);
        config.put("player1", player.toJson());

        super.vertx.deployVerticle(GameVerticle.class.getName(), new DeploymentOptions(config), ar -> {
            if (ar.succeeded()) {
                verticleDeploymentByGUID.put(guid, ar.result());
                LOGGER.info(String.format("Deployed verticle %s for game %s", ar.result(), guid));
                request.reply(new Success().put(GAME_GUID, guid));
            } else {
                request.fail(400, ERROR_MALFORMED_REQUEST);
            }
        });
    }
}
