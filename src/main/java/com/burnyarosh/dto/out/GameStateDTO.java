package com.burnyarosh.dto.out;

import io.vertx.core.json.JsonObject;


//TODO: should this extend success? this could fail during the object mapping...
public class GameStateDTO {
    JsonObject board;
    JsonObject possibleMove;
    String playerGUID;

}
