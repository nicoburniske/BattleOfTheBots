package com.burnyarosh.dto;

import io.vertx.core.json.JsonObject;

public abstract class AbstractDTO extends JsonObject {

    public AbstractDTO() {
        this.put("type", getType());
    }

    protected abstract String getType();
}
