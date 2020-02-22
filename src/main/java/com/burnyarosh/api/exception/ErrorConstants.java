package com.burnyarosh.api.exception;

public interface ErrorConstants {
    String ERROR_PLAYER_REGISTRATION = "Conflict: Username already taken";
    String ERROR_CANNOT_CREATE_GAME = "Conflict: Cannot create new game";
    String ERROR_NO_PLAYER_EXISTS = "Error: Player does not exist";
    String ERROR_MALFORMED_REQUEST = "Malformed request";
    String ERROR_MALFORMED_JSON_REQUEST = "Malformed JSON request";
    String ERROR_INVALID_REQUEST_TYPE = "Error: Invalid Request Type";

    String ERROR_MALFORMED_REQUEST_TEMPLATE = "Malformed request: %s";
    String ERROR_CANNOT_ADD_PLAYER_TO_GAME = "Error: cannot add player to game";
    String ERROR_UNAUTHORIZED_PLAYER = "Error: Unauthorized user";
    String ERROR_INVALID_MOVE = "Error: Invalid Move";
}
