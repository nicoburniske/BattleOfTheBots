package com.burnyarosh.api.exception;

import com.burnyarosh.api.processor.MessageFailureHandler;
import com.burnyarosh.api.processor.WebsocketFailureHandler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.ServerWebSocket;

public class NewPlayerConflictException extends RuntimeException implements HandledException {

    Message message;

    public NewPlayerConflictException(Message message) {
        this.message = message;
    }

    @Override
    public void callSocketHandler(WebsocketFailureHandler handler, ServerWebSocket socket) {
        handler.handleNewPlayerConflict(socket, this);
    }

    @Override
    public void callMessageHandler(MessageFailureHandler handler) {
        handler.handleNewPlayerConflict(message, this);
    }
}
