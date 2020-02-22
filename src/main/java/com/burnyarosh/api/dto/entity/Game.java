package com.burnyarosh.entity;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

public class Game extends Entity {
    private Player[] players = new Player[2];

    public Game(String name, String id, Player first) {
        super(name, id);
        Objects.requireNonNull(first);
        this.players[0] = first;
    }

    public Player getPlayer(int index) {
        if (index != 1 && index != 2) throw new IllegalArgumentException("Invalid player number");
        return this.players[index - 1];
    }

    public void addSecondPlayer(Player player) {
        Objects.requireNonNull(player);
        if (gameIsReady()) {
            throw new IllegalStateException(String.format("Game %s is full", super.id));
        }
        this.players[1] = player;
    }

    public boolean gameIsReady() {
        return players[0] != null && players[1] != null;
    }

    public boolean includesPlayer(String playerGUID) {
        return this.players[0] != null && this.players[0].getId().equals(playerGUID)
                || this.players[1] != null && this.players[1].getId().equals(playerGUID);
    }

    @Override
    public JsonObject toJson() {
        JsonObject game = new JsonObject();
        game.put("gameGUID", this.id);
        JsonArray plyrs = new JsonArray();
        if (players[0] != null)
            plyrs.add(players[0].toJson());
        if (players[1] != null)
            plyrs.add(players[1].toJson());
        game.put("players", plyrs);
        return game;
    }
}
