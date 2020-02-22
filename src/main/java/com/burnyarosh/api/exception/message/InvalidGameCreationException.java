package com.burnyarosh.api.exception.message;

import com.burnyarosh.api.exception.handler.MessageFailureHandler;
import io.vertx.core.eventbus.Message;

public class InvalidGameCreationException extends AbstractMessageException{


    public InvalidGameCreationException(Message message) {
        super(message);
    }

    @Override
    public void callLobbyFailureHandler(MessageFailureHandler handler) {
        handler.handleInvalidGameCreation(super.message);
    }
}
