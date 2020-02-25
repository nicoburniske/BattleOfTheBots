package com.burnyarosh.api.processor;

import com.burnyarosh.api.dto.in.InDTO;
import com.burnyarosh.api.dto.in.NewPlayerDTO;
import com.burnyarosh.api.dto.in.Request;
import com.burnyarosh.api.dto.internal.PlayerUpdateDTO;
import com.burnyarosh.api.dto.out.FailureDTO;

import com.burnyarosh.api.exception.HandledSocketException;
import com.burnyarosh.api.exception.handler.WebsocketFailureHandler;
import com.burnyarosh.api.exception.socket.InvalidRequestException;
import com.burnyarosh.api.processor.utils.Mapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
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

    // Entry points
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

    /**
     * Doesn't do anything special yet. Prints stacktrace.
     *
     * @param throwable to be handled
     */
    private void handlerServerExceptions(Throwable throwable) {
        LOGGER.error(throwable);
    }

    /**
     * Initializes server configuration, registers default codecs for eventBus transmission, and initializes consumer for updating clients/players
     */
    private void initialize() {
        this.addressToGuidMap = new HashMap<>();
        this.playerIDtoSocket = new HashMap<>();
        super.vertx.exceptionHandler(this::handlerServerExceptions);
        getStandardCodecs().forEach(codec -> super.vertx.eventBus().registerDefaultCodec(codec.getClazz(), codec));
        MessageConsumer<PlayerUpdateDTO> updateClients = super.vertx.eventBus().consumer(UPDATE_PLAYERS_ADDRESS.getAddressString());
        updateClients.handler(this::updateClients);
    }

    /**
     * Updates clients with a JsonObject
     * TODO: DTO THIS
     *
     * @param updateMessage contains a PlayerUpdateDTO. Contains a playerGUID and a JsonObject
     */
    private void updateClients(Message<PlayerUpdateDTO> updateMessage) {
        PlayerUpdateDTO update = updateMessage.body();
        sendMessageToClient(update.getJson(), update.getPlayerGUID());
    }

    /**
     * Sends the message to the appropriate Websocket
     *
     * @param message    message to be sent
     * @param playerGUID players GUID
     */
    private void sendMessageToClient(JsonObject message, String playerGUID) {
        this.playerIDtoSocket.get(playerGUID).writeTextMessage(message.toString());
    }

    /**
     * Initializes websocket handlers on new connection
     *
     * @param websocket web socket to be handled
     */
    private void onPlayerConnection(ServerWebSocket websocket) {
        websocket
                //.exceptionHandler(throwable -> this.failureHandler.handleFailure(websocket, throwable))
                .exceptionHandler(Throwable::printStackTrace)
                .frameHandler(frame -> this.newConnectionHandler(frame, websocket))
                .closeHandler(v -> this.dropConnectionHandler(websocket));
    }

    /**
     * Handles a dropped connection.
     * TODO: Clean up lobby and game verticles on player disconnection
     *
     * @param websocket the websocket whose connection was dropped
     */
    private void dropConnectionHandler(ServerWebSocket websocket) {
        String address = websocket.remoteAddress().toString();
        String guid = addressToGuidMap.get(address);
        if (guid != null) {
            addressToGuidMap.remove(address);
        }
        //TODO: publish message to event bus to remove player.
        closeQuietly(websocket);
    }

    /**
     * Handles websocket frames.
     * Initially forces player to register.
     *
     * @param frame     contains json data from client
     * @param websocket associated with a given client
     */
    private void newConnectionHandler(WebSocketFrame frame, ServerWebSocket websocket) {
        try {
            JsonObject request = this.frameToJson(frame);
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
            if (e instanceof HandledSocketException) {
                this.websocketFailureHandler.handleFailure(websocket, (HandledSocketException) e);
            } else {
                websocket.writeTextMessage(new FailureDTO("Invalid JSON").toJsonString());
                LOGGER.error(e);
            }
        }
    }

    /**
     * Connection handler for players who have already been registered.
     *
     * @param frame     the frame containing json data.
     * @param websocket the websocket associated with a given client
     */
    private void establishedConnectionHandler(WebSocketFrame frame, ServerWebSocket websocket) {
        try {
            JsonObject json = this.frameToJson(frame);
            Request type = this.getRequestType(json);
            String currPlayer = this.getCurrPlayerGUID(websocket);
            super.vertx.eventBus().request(type.getAddress().getAddressString(), mapAndVerify(type.getClazz(), json, currPlayer),
                    ar -> {
                        if (ar.failed()) {
                            LOGGER.error("Refusing connection", ar.cause());
                        } else {
                            JsonObject result = (JsonObject) ar.result().body();
                            websocket.writeTextMessage(result.toString());
                        }
                    });
        } catch (Exception e) {
            if (e instanceof HandledSocketException) {
                this.websocketFailureHandler.handleFailure(websocket, (HandledSocketException) e);
            } else {
                websocket.writeTextMessage(new FailureDTO("Invalid JSON").toJsonString());
                LOGGER.error(e);
            }
        }
    }

    /**
     * Ensures that the request maps to a DTO properly, and comes from the player that is bound to the specific connection
     */
    private <T extends InDTO> T mapAndVerify(Class<T> clazz, JsonObject json, String player) {
        T result = Mapper.getJsonAsClass(json, clazz);
        if (!result.isValidRequest()) throw new InvalidRequestException();
        if (!result.isAuthorizedPlayer(player))
            throw new InvalidRequestException(); // should this be another exception that shows unauthorized request (401).
        return clazz.cast(result);
    }

    private Request getRequestType(JsonObject json) {
        if (!json.containsKey(PLAYER_GUID)) throw new InvalidRequestException();
        String type = json.getString("type");
        for (Request req : Request.values()) {
            if (req.getType().equals(type)) return req;
        }
        throw new InvalidRequestException();
    }


    private JsonObject frameToJson(WebSocketFrame frame) {
        String str = frame.textData();
        if (str.equals("null")) throw new InvalidRequestException();
        return new JsonObject(str);
    }

    private void closeQuietly(ServerWebSocket socket) {
        try {
            socket.close();
        } catch (Exception ignored) {

        }
    }

    String getCurrPlayerGUID(ServerWebSocket socket) {
        for (Map.Entry<String, ServerWebSocket> entry : this.playerIDtoSocket.entrySet()) {
            if (socket.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        throw new InvalidRequestException();
    }

}
