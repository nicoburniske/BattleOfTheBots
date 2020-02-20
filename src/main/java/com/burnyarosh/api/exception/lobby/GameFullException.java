package com.burnyarosh.api.exception.lobby;

import com.burnyarosh.api.processor.MessageFailureHandler;
import io.vertx.core.eventbus.Message;

public class GameFullException extends RuntimeException implements HandledLobbyException {
    private Message message;

    public GameFullException(Message message) {
        this.message = message;
    }

    @Override
    public void callLobbyFailureHandler(MessageFailureHandler handler) {
        handler.handleLobbyFull(this.message);
    }
}
