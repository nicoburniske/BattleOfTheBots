package com.burnyarosh.entity;

import io.vertx.core.json.JsonObject;

public class Player extends Entity {

    public Player(String name, String id) {
        super(name, id);
    }

    @Override
    public JsonObject toJson() {
        return new JsonObject().put("username", super.name).put("id", super.id);
    }
}
