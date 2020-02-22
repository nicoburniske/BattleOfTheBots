package com.burnyarosh.api.exception;

import com.burnyarosh.api.exception.handler.WebsocketFailureHandler;
import io.vertx.core.http.ServerWebSocket;


/**
 * Visitor Pattern Exception Handling
 */
public interface HandledSocketException {
    void callSocketHandler(WebsocketFailureHandler handler, ServerWebSocket socket);
}
