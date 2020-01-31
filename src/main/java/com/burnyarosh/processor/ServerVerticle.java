package com.burnyarosh.processor;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import static com.burnyarosh.Config.PORT_NUMBER;

/**
 * Verticle to comprise of
 */
public class ServerVerticle extends AbstractVerticle {
    public final static Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        // Internal configuration
        JsonObject config = this.initializeConfig();

        vertx.createHttpServer()
                .websocketHandler(
                        websocket -> {
                            switch (websocket.path()) {
                                case "game":
                                    this.onPlayerConnection(websocket);
                                default:
                                    LOGGER.warn(String.format("Invalid web socket: %s", websocket.path()));
                                    websocket.reject();
                            }
                        }
                )
                .listen(config.getInteger("PORT"), host -> {
                    if (host.succeeded())
                    LOGGER.info(String.format("Game server initialized on : %d", host.result().actualPort()));
                });
    }

    private JsonObject initializeConfig() {
        JsonObject config = super.config();
        config.getInteger("PORT", PORT_NUMBER);
        return config;
    }

    private void onPlayerConnection(ServerWebSocket websocket) {

    }


}
