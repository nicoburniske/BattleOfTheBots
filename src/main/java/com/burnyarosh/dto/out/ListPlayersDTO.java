package com.burnyarosh.dto.out;

import com.burnyarosh.dto.IDTO;
import io.vertx.core.json.JsonArray;

public class ListPlayersDTO implements IDTO {
    JsonArray players;
    public ListPlayersDTO(JsonArray players){
        this.players = players;
    }

    public JsonArray getPlayers() {
        return players;
    }

    public void setPlayers(JsonArray players) {
        this.players = players;
    }
}
