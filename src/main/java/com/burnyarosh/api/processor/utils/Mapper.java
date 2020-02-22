package com.burnyarosh.api.processor.utils;

import com.burnyarosh.api.exception.socket.JsonMappingException;
import io.vertx.core.json.JsonObject;

public class Mapper {
    /**
     * Attempts to map a given JsonObject to the given class.
     * @throws  IllegalArgumentException if the object cannot be converted, or the jsonObject is empty.
     */
    public static <T> T getJsonAsClass(JsonObject json, Class<T> clazz) {
        if (json != null && !json.isEmpty()) {
            try {
                return json.mapTo(clazz);
            } catch (IllegalArgumentException e) {
                throw new JsonMappingException();
            }
        } else {
            throw new JsonMappingException();
        }
    }
}
