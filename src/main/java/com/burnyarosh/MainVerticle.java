package com.burnyarosh;

import com.burnyarosh.processor.ServerVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
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
    public void start(Promise<Void> promise) throws Exception {
        JsonObject config = this.initializeConfig();
        vertx.deployVerticle(new ServerVerticle());
    }

    private JsonObject initializeConfig() {
        JsonObject config = super.config();
        return config;
    }

    /**
     * Attempts to map a given JsonObject to the given class.
     * @throws  IllegalArgumentException if the object cannot be converted, or the jsonObject is empty.
     */
    public static <T> T getJsonAsClass(JsonObject json, Class<T> clazz) {
        if (json != null && !json.isEmpty()) {
            try {
                return json.mapTo(clazz);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Cannot map to given class");
            }
        } else {
            throw new IllegalArgumentException("Empty json");
        }
    }

}
