package com.burnyarosh.processor;

import com.burnyarosh.dto.Request;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.burnyarosh.Config.PORT_NUMBER;
import static com.burnyarosh.dto.Request.*;
import static com.burnyarosh.processor.EventBusAddress.*;

/**
 * Verticle to comprise of the backend WebSocket server.
 */
public class ServerVerticle extends AbstractVerticle {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);
    private Map<String, String> addressToGuidMap = new HashMap<>();
    private JsonObject config;
    private Map<String, String> requestToEventBusAddress;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        //TODO: figure out rest api requests
        vertx.deployVerticle(new GameLobbyVerticle());
        HttpServer server = vertx.createHttpServer();
        server.websocketHandler(
                // websocket is of type ServerWebSocket
                websocket -> {
                    switch (websocket.path()) {
                        case "/player":
                            this.onPlayerConnection(websocket);
                            break;
                        default:
                            LOGGER.warn(String.format("Invalid web socket: %s", websocket.path()));
                            websocket.reject();
                    }
                })
                .listen(PORT_NUMBER, host -> {
                    if (host.succeeded())
                        LOGGER.info(String.format("Game server initialized on port: %d", host.result().actualPort()));
                });
        this.initialize();
    }

    private void initialize() {
        this.requestToEventBusAddress = new HashMap<>();
        this.requestToEventBusAddress.put(NEW_PLAYER_REQUEST.getType(), NEW_PLAYER_ADDRESS.getAddress());
        this.requestToEventBusAddress.put(NEW_LOBBY_REQUEST.getType(), NEW_LOBBY_ADDRESS.getAddress());
        this.requestToEventBusAddress.put(LIST_PLAYERS_REQUEST.getType(), LIST_PLAYER_ADDRESS.getAddress());
        this.requestToEventBusAddress.put(LIST_LOBBIES_REQUEST.getType(), LIST_LOBBY_ADDRESS.getAddress());
        this.requestToEventBusAddress.put(JOIN_LOBBY_REQUEST.getType(), JOIN_LOBBY_ADDRESS.getAddress());
    }

    private void onPlayerConnection(ServerWebSocket websocket) {
        websocket
                .exceptionHandler(Throwable::printStackTrace)
                .frameHandler(frame -> this.newConnectionHandler(frame, websocket));
    }

    private void newConnectionHandler(WebSocketFrame frame, ServerWebSocket websocket) {
        JsonObject request = this.toJson(frame);
        boolean isValid = (request.getString("type").equals(NEW_PLAYER_REQUEST.getType())
                || request.getString("type").equals(LIST_PLAYERS_REQUEST.getType()));
        if (isValid) {
            super.vertx.eventBus().request(this.requestToEventBusAddress.get(request.getString("type")), request,
                    ar -> {
                        if (ar.failed()) {
                            LOGGER.error("Refusing connection", ar.cause());
                            websocket.writeTextMessage(ar.cause().getMessage());
                        } else {
                            websocket.writeTextMessage(ar.result().body().toString());
                            websocket.frameHandler(newFrame -> this.establishedConnectionHandler(newFrame, websocket));
                        }
                    });
        } else {
            websocket.writeTextMessage("Invalid Request.");
        }
    }

    private void establishedConnectionHandler(WebSocketFrame frame, ServerWebSocket websocket) {
        JsonObject json = this.toJson(frame);
        if (this.isValidRequest(json)) {
            super.vertx.eventBus().request(this.requestToEventBusAddress.get(json.getString("type")), json,
                    ar -> {
                        if (ar.failed()) {
                            LOGGER.error("Refusing connection", ar.cause());
                            websocket.writeTextMessage(ar.cause().getMessage());
                        } else {
                            websocket.writeTextMessage(ar.result().body().toString());
                        }
                    });
        } else {
            websocket.writeTextMessage("Invalid Request.");
        }
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
        if (!request.containsKey("guid")) return false;
        String type = request.getString("type");
        if (type == null || type.equals(NEW_PLAYER_REQUEST.getType())) return false;
        for (Request req : Request.values()) {
            if (req.getType().equals(type)) return true;
        }
        return false;
    }
}
