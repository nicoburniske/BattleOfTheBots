package com.burnyarosh.processor;

import com.burnyarosh.dto.Success;
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
    private final static Logger LOGGER = LoggerFactory.getLogger(GameLobbyVerticle.class);

    List<Player> activePlayers = new ArrayList();
    private Map<String, Game> activeGamesById = new HashMap<>();
    private Map<String, Player> activePlayersById = new HashMap<>();
    private Map<String, Player> activePlayersByName = new HashMap<>();

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

    private void newPlayer(Message<JsonObject> request) {
        JsonObject req = request.body();
        if (!req.containsKey("username")) request.fail(400, "Malformed request");
        for (Player p : activePlayers) {
            if (req.getString("username").equals(p.getName())) request.fail(409, "Conflict: Username already taken");
        }
        Player temp = new Player(req.getString("username"), Player.generateGUID());
        this.activePlayers.add(temp);
        request.reply(new Success().put("guid", temp.getId()));
    }

    private void listPlayers(Message<Void> request) {
        JsonArray result = new JsonArray();
        this.activePlayers.forEach(player -> {
            result.add(player.getName());
        });
        request.reply(new Success().put("players", result));
    }

    private void listLobbies(Message<Void> request) {
        JsonArray result = new JsonArray();
        this.activeGamesById.keySet().forEach(key -> {
            result.add(key);
        });
        request.reply(new Success().put("lobbies", result));
    }

    private JsonObject joinLobby(Message<JsonObject> objectMessage) {
        return null;
    }

    private JsonObject newLobby(Message<JsonObject> objectMessage) {
        super.vertx.deployVerticle(new GameVerticle());
        return null;
    }
}
