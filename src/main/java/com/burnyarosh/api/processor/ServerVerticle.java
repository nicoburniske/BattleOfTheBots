package com.burnyarosh.api.processor;

import com.burnyarosh.api.dto.in.Request;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static com.burnyarosh.ServerConfig.PORT_NUMBER;
import static com.burnyarosh.api.dto.in.Request.*;
import static com.burnyarosh.api.processor.utils.Constants.ERROR_INVALID_REQUEST;
import static com.burnyarosh.api.processor.utils.EventBusAddress.*;

/**
 * Verticle to comprise of the backend WebSocket server.
 */
public class ServerVerticle extends AbstractVerticle {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);
    private final FailureHandler failureHandler = new FailureHandler();
    private Map<String, String> addressToGuidMap = new HashMap<>();
    private Map<String, String> requestToEventBusAddress;
    private Map<String, ServerWebSocket> playerIDtoSocket;

    @Override
    public void start(Promise<Void> promise) throws Exception {
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
        promise.complete();
    }

    private void initialize() {
        this.playerIDtoSocket = new HashMap<>();
        this.requestToEventBusAddress = new HashMap<>();
        this.requestToEventBusAddress.put(NEW_PLAYER_REQUEST.getType(), NEW_PLAYER_ADDRESS.getAddress());
        this.requestToEventBusAddress.put(NEW_LOBBY_REQUEST.getType(), NEW_LOBBY_ADDRESS.getAddress());
        this.requestToEventBusAddress.put(LIST_PLAYERS_REQUEST.getType(), LIST_PLAYER_ADDRESS.getAddress());
        this.requestToEventBusAddress.put(LIST_LOBBIES_REQUEST.getType(), LIST_LOBBY_ADDRESS.getAddress());
        this.requestToEventBusAddress.put(JOIN_LOBBY_REQUEST.getType(), JOIN_LOBBY_ADDRESS.getAddress());
        this.requestToEventBusAddress.put(NEW_MOVE_REQUEST.getType(), NEW_MOVE_ADDRESS.getAddress());

        MessageConsumer<JsonObject> updateClients = vertx.eventBus().consumer(UPDATE_PLAYERS_ADDRESS.getAddress());
        updateClients.handler(this::updateClients);
    }

    private void updateClients(Message<JsonObject> jsonObjectMessage) {
        JsonObject update = jsonObjectMessage.body();
        JsonObject state = update.getJsonObject("state");
        JsonArray players = update.getJsonArray("players");
        for(int i = 0; i < players.size(); i++) {
            sendMessageToClient(state, players.getString(i));
        }
    }

    private void sendMessageToClient(JsonObject message, String playerGUID) {
        this.playerIDtoSocket.get(playerGUID).writeTextMessage(message.toString());
    }

    private void onPlayerConnection(ServerWebSocket websocket) {
        websocket
                .exceptionHandler(throwable -> this.failureHandler.handleFailure(websocket, throwable))
                .frameHandler(frame -> this.newConnectionHandler(frame, websocket))
                .closeHandler(v -> this.dropConnectionHandler(websocket));
    }

    private void dropConnectionHandler(ServerWebSocket websocket) {
        String address = websocket.remoteAddress().toString();
        String guid = addressToGuidMap.get(address);
        if (guid != null) {
            addressToGuidMap.remove(address);
        }
        //TODO: publish message to event bus to remove player.
    }

    private void newConnectionHandler(WebSocketFrame frame, ServerWebSocket websocket) {
        JsonObject request = this.toJson(frame);
        boolean isValid = (request.getString("type").equals(NEW_PLAYER_REQUEST.getType()));
        if (isValid) {
            super.vertx.eventBus().request(String.valueOf(NEW_PLAYER_ADDRESS), request,
                    ar -> {
                        if (ar.failed()) {
                            LOGGER.error("Refusing connection", ar.cause());
                            websocket.writeTextMessage(ar.cause().getMessage());
                        } else {
                            JsonObject result = (JsonObject) ar.result().body();
                            String playerGUID = result.getString("playerGUID");
                            this.addressToGuidMap.put(websocket.remoteAddress().toString(), playerGUID);
                            this.playerIDtoSocket.put(playerGUID, websocket);
                            websocket.writeTextMessage(result.toString());
                            websocket.frameHandler(newFrame -> this.establishedConnectionHandler(newFrame, websocket));
                        }
                    });
        } else {
            websocket.writeTextMessage(ERROR_INVALID_REQUEST);
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
            websocket.writeTextMessage(ERROR_INVALID_REQUEST);
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
        if (!request.containsKey("playerGUID")) return false;
        String type = request.getString("type");
        if (type == null || type.equals(NEW_PLAYER_REQUEST.getType())) return false;
        for (Request req : Request.values()) {
            if (req.getType().equals(type)) return true;
        }
        return false;
    }
}
