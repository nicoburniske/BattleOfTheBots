package com.burnyarosh.api.exception;

import com.burnyarosh.api.processor.MessageFailureHandler;
import com.burnyarosh.api.processor.WebsocketFailureHandler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.ServerWebSocket;


/**
 * Visitor Pattern Exception Handling
 */
public interface HandledException {
    void callSocketHandler(WebsocketFailureHandler handler, ServerWebSocket socket);
}
