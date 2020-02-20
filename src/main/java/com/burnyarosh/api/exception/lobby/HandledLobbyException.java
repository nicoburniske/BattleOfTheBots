package com.burnyarosh.api.exception.lobby;

import com.burnyarosh.api.processor.MessageFailureHandler;
import io.vertx.core.eventbus.Message;

public interface HandledLobbyException {
    void callLobbyFailureHandler(MessageFailureHandler handler);
}
