package com.burnyarosh.processor;

public enum EventbusAddy {
    NEW_PLAYER("new_player"),
    NEW_MOVE("make_move"),
    JOIN_LOBBY("join_lobby"),
    LIST_LOBBY("list_lobbies"),
    LIST_PLAYERS("list_players");

    private String type;

    EventbusAddy(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
