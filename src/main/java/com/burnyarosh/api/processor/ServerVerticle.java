package com.burnyarosh.api.processor;

import com.burnyarosh.api.dto.IDTO;
import com.burnyarosh.api.dto.in.InDTO;
import com.burnyarosh.api.dto.in.NewPlayerDTO;
import com.burnyarosh.api.dto.in.Request;
import com.burnyarosh.api.dto.out.FailureDTO;
import com.burnyarosh.api.exception.HandledException;
import com.burnyarosh.api.exception.InvalidRequestException;
import com.burnyarosh.api.processor.utils.Mapper;
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
import static com.burnyarosh.api.dto.codec.StandardCodecs.getStandardCodecs;
import static com.burnyarosh.api.processor.utils.Constants.PLAYER_GUID;
import static com.burnyarosh.api.processor.utils.EventBusAddress.*;

/**
 * Verticle to comprise of the backend WebSocket server.
 */
public class ServerVerticle extends AbstractVerticle {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);
    private final WebsocketFailureHandler websocketFailureHandler = new WebsocketFailureHandler();
    private Map<String, String> addressToGuidMap;
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

    private void handlerServerExceptions(Throwable throwable) {
        System.out.println("HECK");
    }

    private void initialize() {
        this.addressToGuidMap = new HashMap<>();
        this.playerIDtoSocket = new HashMap<>();
        super.vertx.exceptionHandler();
        getStandardCodecs().stream().forEach(codec -> super.vertx.eventBus().registerDefaultCodec(codec.getClazz(), codec));
        MessageConsumer<JsonObject> updateClients = super.vertx.eventBus().consumer(UPDATE_PLAYERS_ADDRESS.getAddressString());
        updateClients.handler(this::updateClients);
    }

    private void updateClients(Message<JsonObject> jsonObjectMessage) {
        JsonObject update = jsonObjectMessage.body();
        JsonObject state = update.getJsonObject("state");
        JsonArray players = update.getJsonArray("players");
        for (int i = 0; i < players.size(); i++) {
            sendMessageToClient(state, players.getString(i));
        }
    }

    private void sendMessageToClient(JsonObject message, String playerGUID) {
        this.playerIDtoSocket.get(playerGUID).writeTextMessage(message.toString());
    }

    private void onPlayerConnection(ServerWebSocket websocket) {
        websocket
                //.exceptionHandler(throwable -> this.failureHandler.handleFailure(websocket, throwable))
                .exceptionHandler(Throwable::printStackTrace)
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
        closeQuietly(websocket);
    }

    private void handlerConnectionExceptions(ServerWebSocket socket, Throwable throwable) {

    }

    private void newConnectionHandler(WebSocketFrame frame, ServerWebSocket websocket) {
        try {
            JsonObject request = this.toJson(frame);
            NewPlayerDTO dto = Mapper.getJsonAsClass(request, NewPlayerDTO.class);
            super.vertx.eventBus().request(NEW_PLAYER_ADDRESS.getAddressString(), dto,
                    ar -> {
                        if (ar.failed()) {
                            LOGGER.error("Refusing connection", ar.cause());
                            websocket.writeTextMessage(ar.cause().getMessage());
                        } else {
                            JsonObject result = (JsonObject) ar.result().body();
                            String guid = result.getString(PLAYER_GUID);
                            this.addressToGuidMap.put(websocket.remoteAddress().toString(), guid);
                            this.playerIDtoSocket.put(result.getString(PLAYER_GUID), websocket);
                            websocket.writeTextMessage(result.toString());
                            // change future frame handler because user is now "logged" in
                            websocket.frameHandler(newFrame -> this.establishedConnectionHandler(newFrame, websocket));
                        }
                    });
        } catch (Exception e) {
            if (e instanceof HandledException) {
                this.websocketFailureHandler.handleFailure(websocket, (HandledException) e);
            } else {
                LOGGER.error(e.getMessage(), (Object[]) e.getStackTrace());
            }
        }
    }

    private void establishedConnectionHandler(WebSocketFrame frame, ServerWebSocket websocket) {
        try {
            JsonObject json = this.toJson(frame);
            Request type = this.getRequestType(json);
            // TODO: is cast necessary
            super.vertx.eventBus().request(type.getAddress().getAddressString(), mapAndVerify(type.getClazz(), json),
                    ar -> {
                        if (ar.failed()) {
                            LOGGER.error("Refusing connection", ar.cause());
                        } else {
                            JsonObject result = (JsonObject) ar.result().body();
                            websocket.writeTextMessage(result.toString());
                        }
                    });
        } catch (Exception e) {
            if (e instanceof HandledException) {
                this.websocketFailureHandler.handleFailure(websocket, (HandledException) e);
            } else {
                websocket.writeTextMessage(new FailureDTO("Invalid JSON").toJsonString());
                LOGGER.error(e);
            }
        }
    }

    private  <T extends InDTO> T mapAndVerify(Class<T> clazz, JsonObject json) {
        T result = Mapper.getJsonAsClass(json, clazz);
        if (!result.isValidRequest()) throw new InvalidRequestException();
        return clazz.cast(Mapper.getJsonAsClass(json, clazz));
    }

    private Request getRequestType(JsonObject json) {
        if (!json.containsKey(PLAYER_GUID)) throw new InvalidRequestException();
        String type = json.getString("type");
        for (Request req : Request.values()) {
            if (req.getType().equals(type)) return req;
        }
        throw new InvalidRequestException();
    }


    private JsonObject toJson(WebSocketFrame frame) {
        String str = frame.textData();
        if (str.equals("null")) throw new InvalidRequestException();
        return new JsonObject(str);
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
}
