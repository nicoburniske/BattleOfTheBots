package com.burnyarosh.api.exception.lobby;

import com.burnyarosh.api.processor.MessageFailureHandler;
import io.vertx.core.eventbus.Message;

public class NewPlayerConflictException extends RuntimeException implements HandledLobbyException {

    private Message message;

    public NewPlayerConflictException(Message message) {
        this.message = message;
    }

    @Override
    public void callLobbyFailureHandler(MessageFailureHandler handler) {
        handler.handleNewPlayerConflict(this.message, this);
    }
}
