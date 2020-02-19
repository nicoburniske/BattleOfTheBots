package com.burnyarosh.api.processor.utils;

import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class VerticleConfig {
    private Map<String, JsonObject> verticleIDtoConfig;

    public VerticleConfig() {
        this.verticleIDtoConfig = new HashMap<>();
    }

    public void putConfig(String id, JsonObject config) {
        if (id == null || config == null) throw new IllegalArgumentException();
        this.verticleIDtoConfig.put(id, config);
    }

    public JsonObject getConfig(String id) {
        if (id == null) throw new IllegalArgumentException();
        return this.verticleIDtoConfig.get(id);
    }
}
