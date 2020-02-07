package com.burnyarosh.dto.out.lobby;

import com.burnyarosh.dto.IDTO;
import io.vertx.core.json.JsonArray;

public class ListLobbiesDTO implements IDTO {
    JsonArray lobbies;
    public ListLobbiesDTO(JsonArray lobbies){
        this.lobbies = this.lobbies;
    }

    public JsonArray getLobbies() {
        return lobbies;
    }

    public void setLobbies(JsonArray lobbies) {
        this.lobbies = lobbies;
    }
}
