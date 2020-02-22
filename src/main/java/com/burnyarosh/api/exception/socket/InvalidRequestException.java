package com.burnyarosh.api.exception.socket;

import com.burnyarosh.api.exception.HandledSocketException;
import com.burnyarosh.api.exception.handler.WebsocketFailureHandler;
import io.vertx.core.http.ServerWebSocket;

public class InvalidRequestException extends RuntimeException implements HandledSocketException {
    @Override
    public void callSocketHandler(WebsocketFailureHandler handler, ServerWebSocket socket) {
        handler.handleInvalidRequest(socket);
    }
}
