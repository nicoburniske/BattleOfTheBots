package com.burnyarosh.api.exception.message;

import com.burnyarosh.api.exception.HandledMessageException;
import io.vertx.core.eventbus.Message;

public abstract class AbstractMessageException extends RuntimeException implements HandledMessageException {
    protected Message message;

    protected AbstractMessageException(Message message) {
        this.message = message;
    }
}
