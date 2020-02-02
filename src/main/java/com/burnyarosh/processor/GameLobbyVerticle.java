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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.burnyarosh.processor.EventBusAddress.*;

public class GameLobbyVerticle extends AbstractVerticle {
    List<Player> activePlayers = new ArrayList();
    private Map<String, Game> activeGamesById = new HashMap<>();
    private Map<String, Game> activeGamesByName = new HashMap<>();
    private Map<String, Player> activePlayersById = new HashMap<>();
    private Map<String, Player> activePlayersByName = new HashMap<>();

    @Override
    public void start(Future<Void> future) throws Exception {
        MessageConsumer<JsonObject> newPlayer = vertx.eventBus().consumer(NEW_PLAYER.getType());
        newPlayer.handler(this::newPlayer);

        MessageConsumer<JsonObject> newLobby = vertx.eventBus().consumer(NEW_LOBBY.getType());
        newLobby.handler(this::newLobby);

        MessageConsumer<JsonObject> joinLobby = vertx.eventBus().consumer(JOIN_LOBBY.getType());
        joinLobby.handler(this::joinLobby);

        MessageConsumer<JsonObject> listLobbies = vertx.eventBus().consumer(LIST_LOBBIES.getType());
        listLobbies.handler(this::listLobbies);

        MessageConsumer<JsonObject> listPlayers = vertx.eventBus().consumer(JOIN_LOBBY.getType());
        listPlayers.handler(this::listPlayers);
    }

    private void newPlayer(Message<JsonObject> request) {
        JsonObject req = request.body();
        for (Player p : activePlayers) {
            if (req.getString("username").equals(p.getName())) request.fail(409, "Conflict");
        }
        Player temp = new Player(req.getString("username"), Player.generateGUID());
        this.activePlayers.add(temp);
        request.reply(new Success().put("key", temp.getId()));
    }

    private JsonArray listPlayers(Message<JsonObject> objectMessage) {
        JsonArray result = new JsonArray();
        this.activePlayersByName.keySet().forEach(key -> {
            result.add(key);
        });
        return result;
    }

    private JsonArray listLobbies(Message<JsonObject> objectMessage) {
        objectMessage.body();
        JsonArray result = new JsonArray();
        this.activeGamesByName.keySet().forEach(key -> {
            result.add(key);
        });
        return result;
    }

    private JsonObject joinLobby(Message<JsonObject> objectMessage) {
        return null;
    }

    private JsonObject newLobby(Message<JsonObject> objectMessage) {
        super.vertx.deployVerticle(new GameVerticle());
        return null;
    }
}
