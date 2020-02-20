package com.burnyarosh.api.exception.lobby;

import com.burnyarosh.api.processor.MessageFailureHandler;
import io.vertx.core.eventbus.Message;

public class InvalidMoveException extends RuntimeException implements HandledLobbyException {
    Message message;

    public InvalidMoveException(Message message) {
        this.message = message;
    }

    @Override
    public void callLobbyFailureHandler(MessageFailureHandler handler) {
        handler.handleInvalidMove(message);
    }
}
