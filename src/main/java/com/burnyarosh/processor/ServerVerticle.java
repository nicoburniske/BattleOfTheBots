package com.burnyarosh.processor;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.burnyarosh.Config.PORT_NUMBER;
import static com.burnyarosh.processor.EventBusAddress.NEW_PLAYER;

/**
 * Verticle to comprise of the backend WebSocket server.
 */
public class ServerVerticle extends AbstractVerticle {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);
    private Map<String, String> addressToGuidMap = new HashMap<>();
    private JsonObject config;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        /*Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());*/
        vertx.deployVerticle(new GameLobbyVerticle());
        HttpServer server = vertx.createHttpServer();
        server.websocketHandler(
                // websocket is of type ServerWebSocket
                websocket -> {
                    switch (websocket.path()) {
                        case "/player":
                            this.onPlayerConnection(websocket);
                            break;
                        case "/player/game":
                            this.handlePlayerRequest(websocket);
                            break;
                        default:
                            LOGGER.warn(String.format("Invalid web socket: %s", websocket.path()));
                            websocket.reject();
                    }
                }
        )
                .listen(PORT_NUMBER, host -> {
                    if (host.succeeded())
                        LOGGER.info(String.format("Game server initialized on port: %d", host.result().actualPort()));
                });
    }

    private void handlePlayerRequest(ServerWebSocket websocket) {
        websocket
                .exceptionHandler(Throwable::printStackTrace)
                .frameHandler(frame -> {
                    JsonObject json = this.toJson(frame);
                    if (this.isValidRequest(json)) {
                        super.vertx.eventBus().request(json.getString("type"), new DeliveryOptions().setSendTimeout(10000),
                                ar -> {
                                    if (ar.failed()) {
                                        LOGGER.error("Refusing connection", ar.cause());
                                        this.closeQuietly(websocket);
                                    } else {

                                    }
                                });
                    }
                    ;
                });
        websocket.accept();
        websocket.writeTextMessage(websocket.remoteAddress().toString());
    }

    private void onPlayerConnection(ServerWebSocket websocket) {
        websocket
                .exceptionHandler(Throwable::printStackTrace)
                .frameHandler(frame -> {
                    JsonObject json = this.toJson(frame);
                    if (this.isValidRequest(json)) {
                        super.vertx.eventBus().request(NEW_PLAYER.getType(), json,
                                ar -> {
                                    if (ar.failed()) {
                                        LOGGER.error("Refusing connection", ar.cause());
                                        this.closeQuietly(websocket);
                                    } else {
                                        websocket.writeTextMessage(ar.result().body().toString());
                                    }
                                });
                    }
                    ;
                });
        websocket.accept();
        websocket.writeTextMessage(websocket.remoteAddress().toString());
    }

    private Handler<AsyncResult<Message<String>>> configureRegistration(AsyncResult<Object> result) {
        return null;
    }

    private JsonObject toJson(WebSocketFrame frame) {
        return new JsonObject(frame.textData());
    }

    private void closeLoudly(ServerWebSocket socket) throws Exception {
        socket.close();
    }

    private void closeQuietly(ServerWebSocket socket) {
        try {
            socket.close();
        } catch (Exception ignored) {

        }
    }

    private boolean isValidRequest(JsonObject request) {
        try {
            String type = request.getString("type");
            for (EventBusAddress req : EventBusAddress.values()) {
                if (req.getType().equals(type)) return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
