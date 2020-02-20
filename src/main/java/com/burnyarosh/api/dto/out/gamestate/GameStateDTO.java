package com.burnyarosh.api.dto.out.gamestate;

import io.vertx.core.json.JsonObject;


public class GameStateDTO {
    JsonObject board;
    JsonObject possibleMove;
    String playerGUID;
}
