package com.burnyarosh.api.dto;

import io.vertx.core.json.JsonObject;

public interface IDTO {
    default JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }
    default String toJsonString() { return JsonObject.mapFrom(this).encode();}
}
