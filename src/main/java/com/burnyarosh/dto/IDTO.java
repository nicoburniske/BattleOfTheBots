package com.burnyarosh.dto;

import io.vertx.core.json.JsonObject;

public interface IDTO {
    default JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }
}
