package com.burnyarosh.api.exception.message;

import com.burnyarosh.api.exception.handler.MessageFailureHandler;
import io.vertx.core.eventbus.Message;

public class GameFullException extends AbstractMessageException {


    public GameFullException(Message message) {
        super(message);
    }

    @Override
    public void callLobbyFailureHandler(MessageFailureHandler handler) {
        handler.handleLobbyFull(super.message);
    }
}
