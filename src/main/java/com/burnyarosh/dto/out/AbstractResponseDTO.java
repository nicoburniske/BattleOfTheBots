package com.burnyarosh.dto.out;

import io.vertx.core.json.JsonObject;

public abstract class AbstractResponseDTO extends JsonObject {

    public AbstractResponseDTO() {
        this.put("status", getStatus());
    }

    protected abstract String getStatus();
}
