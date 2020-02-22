package com.burnyarosh.api.exception.handler;

import com.burnyarosh.api.dto.out.FailureDTO;
import com.burnyarosh.api.exception.HandledSocketException;
import io.vertx.core.http.ServerWebSocket;

import static com.burnyarosh.api.exception.ErrorConstants.*;


public class WebsocketFailureHandler {

    public void handleFailure(ServerWebSocket websocket, HandledSocketException throwable) {
            throwable.callSocketHandler(this, websocket);
    }

    public void handleInvalidJsonMapping(ServerWebSocket socket) {
        FailureDTO failure = new FailureDTO(ERROR_MALFORMED_JSON_REQUEST);
        socket.writeTextMessage(failure.toJsonString());
    }

    public void handleInvalidRequest(ServerWebSocket socket) {
        FailureDTO failure = new FailureDTO(ERROR_INVALID_REQUEST_TYPE);
        socket.writeTextMessage(failure.toJsonString());
    }
}
