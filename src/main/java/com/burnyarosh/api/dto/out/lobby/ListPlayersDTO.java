package com.burnyarosh.api.dto.out.lobby;

import com.burnyarosh.api.dto.IDTO;

import java.util.List;

public class ListPlayersDTO implements IDTO {
    List<String> players;

    public ListPlayersDTO(List<String> players) {
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
