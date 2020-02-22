package com.burnyarosh.api.exception;

import com.burnyarosh.api.exception.handler.MessageFailureHandler;

public interface HandledMessageException {
    void callLobbyFailureHandler(MessageFailureHandler handler);
}
