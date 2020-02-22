package com.burnyarosh.entity;

import io.vertx.core.json.JsonObject;

public class Player extends Entity {
    private boolean isBusy;
    public Player(String name, String id) {
        super(name, id);
        this.isBusy = false;
    }

    public boolean getIsBusy() {
        return this.isBusy;
    }

    public void setBusy(boolean busy) {
        this.isBusy = busy;
    }

    @Override
    public JsonObject toJson() {
        return new JsonObject().put("username", super.name).put("id", super.id);
    }
}
