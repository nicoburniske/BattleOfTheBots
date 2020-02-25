package com.burnyarosh.api.processor.utils;

public enum EventBusAddress {
    NEW_PLAYER_ADDRESS("burnyarosh.lobby.new.player"),
    NEW_MOVE_ADDRESS("burnyarosh.lobby.move"),
    JOIN_LOBBY_ADDRESS("burnyarosh.lobby.join"),
    JOIN_ANY_LOBBY("burnyarosh.lobby.join.any"),
    REMOVE_PLAYER_ADDRESS("burnyarosh.lobby.remove.player"),
    NEW_LOBBY_ADDRESS("burnyarosh.lobby.new.lobby"),
    LIST_LOBBY_ADDRESS("burnyarosh.lobby.list.lobby"),
    LIST_PLAYER_ADDRESS("burnyarosh.lobby.list.player"),
    GAME_BASE_ADDRESS("burnyarosh.game.%s"),
    GAME_MOVE_ADDRESS("burnyarosh.game.%s.move"),
    UPDATE_PLAYERS_ADDRESS("burnyarosh.player.connection.update");

    private String address;

    EventBusAddress(String type) {
        this.address = type;
    }

    public String getAddressString() {
        return this.address;
    }
}
