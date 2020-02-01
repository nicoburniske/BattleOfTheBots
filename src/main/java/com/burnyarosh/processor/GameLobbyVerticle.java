package com.burnyarosh.processor;

import com.burnyarosh.entity.Game;
import com.burnyarosh.entity.Player;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;

import java.util.HashMap;
import java.util.Map;

public class GameLobbyVerticle extends AbstractVerticle {
    private Map<String, Game> activeGamesById = new HashMap<>();
    private Map<String, Game> activeGamesByName = new HashMap<>();
    private Map<String, Player> activePlayersById = new HashMap<>();
    private Map<String, Player> activePlayersByName = new HashMap<>();

    @Override
    public void start(Future<Void> future) throws Exception {
        vertx.eventBus().consumer("new_lobby").handler(this::handleNewGame);
        vertx.eventBus().consumer("join_lobby").handler(this::joinGame);
        vertx.eventBus().consumer("list_lobbies").handler(this::listLobbies);
        vertx.eventBus().consumer("list_players").handler(this::listPlayers);
    }

    private JsonArray listPlayers(Message<Object> objectMessage) {
        JsonArray result = new JsonArray();
        this.activePlayersByName.keySet().forEach(key -> {
            result.add(key);
        });
        return result;
    }

    private JsonArray listLobbies(Message<Object> objectMessage) {
        objectMessage.body();
        JsonArray result = new JsonArray();
        this.activeGamesByName.keySet().forEach(key -> {
            result.add(key);
        });
        return result;
    }

    private void joinGame(Message<Object> objectMessage) {

    }

    private void handleNewGame(Message<Object> objectMessage) {
    }
}
