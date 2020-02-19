package com.burnyarosh.dto.out.lobby;

import com.burnyarosh.dto.IDTO;

import java.util.List;

public class ListPlayersDTO implements IDTO {
    List<String> players;
    public ListPlayersDTO(List<String> players){
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }
}
