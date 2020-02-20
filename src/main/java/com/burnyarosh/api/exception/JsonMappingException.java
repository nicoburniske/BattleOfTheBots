package com.burnyarosh.api.exception;

import com.burnyarosh.api.processor.MessageFailureHandler;
import com.burnyarosh.api.processor.WebsocketFailureHandler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.ServerWebSocket;

public class JsonMappingException extends RuntimeException implements HandledException {
    @Override
    public void callSocketHandler(WebsocketFailureHandler handler, ServerWebSocket socket) {
        handler.handleInvalidJsonMapping(socket);
    }
}
