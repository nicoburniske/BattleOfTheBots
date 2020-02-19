package com.burnyarosh.api.exception;

import com.burnyarosh.api.processor.FailureHandler;
import io.vertx.core.http.ServerWebSocket;


/**
 * Visitor Pattern Exception Handling
 */
public interface HandledException {
    void callHandler(FailureHandler handler, ServerWebSocket socket);
}
