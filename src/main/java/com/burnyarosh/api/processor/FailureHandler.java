package com.burnyarosh.api.processor;

import com.burnyarosh.api.dto.out.FailureDTO;
import com.burnyarosh.api.exception.HandledException;
import io.vertx.core.http.ServerWebSocket;



public class FailureHandler {

    public void handleFailure(ServerWebSocket websocket, Throwable throwable) {
        if (throwable instanceof HandledException) {
            ((HandledException) throwable).callHandler(this, websocket);
        } else {

        }
    }

    public void handleInvalidJsonMapping(ServerWebSocket socket) {
        FailureDTO failure = new FailureDTO();
        socket.writeTextMessage("Invalid Json request");
    }
}
