package com.burnyarosh.api.dto.internal;

import com.burnyarosh.api.dto.IDTO;
import io.vertx.core.json.JsonObject;

public class PlayerUpdateDTO implements IDTO {
    private String PlayerGUID;
    private JsonObject json;

    public String getPlayerGUID() {
        return PlayerGUID;
    }

    public void setPlayerGUID(String playerGUID) {
        PlayerGUID = playerGUID;
    }

    public JsonObject getJson() {
        return json;
    }

    public void setJson(JsonObject json) {
        this.json = json;
    }
}
