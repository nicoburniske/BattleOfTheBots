package com.burnyarosh.api.processor;

import com.burnyarosh.api.dto.out.FailureDTO;
import com.burnyarosh.api.exception.HandledException;
import com.burnyarosh.api.exception.NewPlayerConflictException;
import io.vertx.core.http.ServerWebSocket;



public class FailureHandler {

    public void handleFailure(ServerWebSocket websocket, HandledException throwable) {
            throwable.callHandlerSocket(this, websocket);
    }

    public void handleInvalidJsonMapping(ServerWebSocket socket) {
        FailureDTO failure = new FailureDTO("Malformed Json request");
        socket.writeTextMessage(failure.toJsonString());
    }

    public void handleNewPlayerConflict(ServerWebSocket socket, NewPlayerConflictException e) {
        FailureDTO failure = new FailureDTO("Username already taken");
        socket.writeTextMessage(failure.toJsonString());
    }

    public void handleInvalidRequest(ServerWebSocket socket) {
        FailureDTO failure = new FailureDTO("Invalid request type");
        socket.writeTextMessage(failure.toJsonString());
    }
}
