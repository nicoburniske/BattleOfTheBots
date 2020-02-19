package com.burnyarosh.api.processor.utils;

public enum EventBusAddress {
    NEW_PLAYER_ADDRESS("new.player"),
    NEW_MOVE_ADDRESS("new.move"),
    JOIN_LOBBY_ADDRESS("join.lobby"),
    JOIN_ANY_LOBBY("join.any.lobby"),
    NEW_LOBBY_ADDRESS("new.lobby"),
    LIST_LOBBY_ADDRESS("list.lobby"),
    LIST_PLAYER_ADDRESS("list.player"),
    LOBBY_BASE_ADDRESS("burnyarosh.lobby."),
    UPDATE_PLAYERS_ADDRESS("burnyarosh.player.connection.update");

    private String address;

    EventBusAddress(String type) {
        this.address = type;
    }

    public String getAddress() {
        return this.address;
    }
}
