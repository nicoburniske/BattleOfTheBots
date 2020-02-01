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
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

import static com.burnyarosh.Config.PORT_NUMBER;

/**
 * Verticle to comprise of the backend WebSocket server.
 */
public class ServerVerticle extends AbstractVerticle {
    public final static Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);
    private Map<String, String> addressToGuidMap = new HashMap<>();
    private JsonObject config;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        HttpServer server = vertx.createHttpServer();
        server.websocketHandler(
                // websocket is of type ServerWebSocket
                websocket -> {
                    // handle handshake. if valid username generate player
                    // websocket.setHandshake(Future.future(), this.handleHandShake(websocket));
                    switch (websocket.path()) {
                        case "/player":
                            this.onPlayerConnection(websocket);
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

    private void onPlayerConnection(ServerWebSocket websocket) {
        websocket
                .exceptionHandler(Throwable::printStackTrace)
                .frameHandler( frame -> {
                  super.vertx.eventBus().request("register.player",
                          new JsonObject().put("event", "login").put("message", this.toJson(frame)), new DeliveryOptions().setSendTimeout(10000),
                          ar -> {
                            if (ar.failed()) {
                                LOGGER.error("Refusing connection", ar.cause());
                            } else {

                            }
                          });
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
}
