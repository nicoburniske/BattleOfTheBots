package com.burnyarosh.api.dto.in;

import com.burnyarosh.api.processor.utils.EventBusAddress;

public enum Request {
    NEW_PLAYER_REQUEST("new_player", EventBusAddress.NEW_PLAYER_ADDRESS, NewPlayerDTO.class),
    NEW_MOVE_REQUEST("new_move", EventBusAddress.NEW_MOVE_ADDRESS, PlayerTurnDTO.class),
    JOIN_LOBBY_REQUEST("join_lobby", EventBusAddress.JOIN_LOBBY_ADDRESS, JoinLobbyDTO.class),
    NEW_LOBBY_REQUEST("new_lobby", EventBusAddress.NEW_LOBBY_ADDRESS, NewLobbyDTO.class),
    LIST_LOBBIES_REQUEST("list_lobby", EventBusAddress.LIST_LOBBY_ADDRESS, RequestDTOImpl.class),
    LIST_PLAYERS_REQUEST("list_player", EventBusAddress.LIST_PLAYER_ADDRESS, RequestDTOImpl.class);

    private String type;
    private EventBusAddress address;
    private Class clazz;

    Request(String type, EventBusAddress address, Class clazz) {
        this.type = type;
        this.address = address;
        this.clazz = clazz;
    }

    public String getType() {
        return this.type;
    }

    public EventBusAddress getAddress() {
        return address;
    }

    public Class getClazz() {
        return clazz;
    }
}
