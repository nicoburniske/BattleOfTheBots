package com.burnyarosh.api.exception.message;

import com.burnyarosh.api.exception.handler.MessageFailureHandler;
import io.vertx.core.eventbus.Message;

public class NewPlayerConflictException extends AbstractMessageException {

    public NewPlayerConflictException(Message message) {
        super(message);
    }

    @Override
    public void callLobbyFailureHandler(MessageFailureHandler handler) {
        handler.handleNewPlayerConflict(super.message, this);
    }
}
