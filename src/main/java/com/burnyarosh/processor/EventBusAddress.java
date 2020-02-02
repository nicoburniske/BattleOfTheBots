package com.burnyarosh.processor;

public enum EventBusAddress {
    NEW_PLAYER("new_player"),
    NEW_MOVE("make_move"),
    JOIN_LOBBY("join_lobby"),
    NEW_LOBBY("new_lobby"),
    LIST_LOBBIES("list_lobbies"),
    LIST_PLAYERS("list_players");

    private String type;

    EventBusAddress(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
