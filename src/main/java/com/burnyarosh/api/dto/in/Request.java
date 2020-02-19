package com.burnyarosh.api.dto.in;

public enum Request {
    NEW_PLAYER_REQUEST("new_player"),
    NEW_MOVE_REQUEST("new_move"),
    JOIN_LOBBY_REQUEST("join_lobby"),
    NEW_LOBBY_REQUEST("new_lobby"),
    LIST_LOBBIES_REQUEST("list_lobby"),
    LIST_PLAYERS_REQUEST("list_player");

    private String type;

    Request(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
