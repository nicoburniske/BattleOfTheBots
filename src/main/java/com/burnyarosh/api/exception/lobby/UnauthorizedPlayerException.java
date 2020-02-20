package com.burnyarosh.api.exception.lobby;

import com.burnyarosh.api.processor.MessageFailureHandler;
import io.vertx.core.eventbus.Message;

public class UnauthorizedPlayerException extends RuntimeException implements HandledLobbyException {

    Message message;

    public UnauthorizedPlayerException(Message message1) {
        this.message = message1;
    }

    @Override
    public void callLobbyFailureHandler(MessageFailureHandler handler) {
        handler.handleUnauthorizedPlayer(this.message);
    }
}
