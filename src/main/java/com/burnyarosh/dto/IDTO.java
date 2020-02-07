package com.burnyarosh.dto;

import io.vertx.core.json.JsonObject;

public interface IDTO {
    default String toJson() {
        return JsonObject.mapFrom(this).encode();
    }
}
