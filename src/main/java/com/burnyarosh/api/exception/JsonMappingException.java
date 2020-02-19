package com.burnyarosh.api.exception;

import com.burnyarosh.api.processor.FailureHandler;
import io.vertx.core.http.ServerWebSocket;

public class JsonMappingException extends RuntimeException implements HandledException {
    @Override
    public void callHandler(FailureHandler handler, ServerWebSocket socket) {
        handler.handleInvalidJsonMapping(socket);
    }
}
