package com.burnyarosh.api.exception.message;

import com.burnyarosh.api.exception.handler.MessageFailureHandler;
import io.vertx.core.eventbus.Message;

public class UnauthorizedPlayerException extends AbstractMessageException {

    public UnauthorizedPlayerException(Message message) {
        super(message);
    }

    @Override
    public void callLobbyFailureHandler(MessageFailureHandler handler) {
        handler.handleUnauthorizedPlayer(super.message);
    }
}
