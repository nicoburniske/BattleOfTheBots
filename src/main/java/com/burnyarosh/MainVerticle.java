package com.burnyarosh;

import com.burnyarosh.processor.ServerVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class MainVerticle extends AbstractVerticle {

    /**
     * Convenience method to be able to run instance in IDE
     * @param args
     */
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        JsonObject config = this.initializeConfig();
        vertx.deployVerticle(new ServerVerticle());
    }

    private JsonObject initializeConfig() {
        JsonObject config = super.config();
        return config;
    }
}
