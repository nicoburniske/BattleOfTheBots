package com.burnyarosh;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public class MainVerticle extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        // Internal configuration
        JsonObject config = this.initializeConfig();

    }

    private JsonObject initializeConfig() {
        JsonObject config = super.config();

        return config;
    }
}
