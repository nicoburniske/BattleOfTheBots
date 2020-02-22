package com.burnyarosh.api.exception.handler;

import com.burnyarosh.api.dto.out.FailureDTO;
import com.burnyarosh.api.exception.message.NewPlayerConflictException;
import io.vertx.core.eventbus.Message;

import static com.burnyarosh.api.exception.ErrorConstants.*;

public class MessageFailureHandler {

    public void handleNewPlayerConflict(Message message, NewPlayerConflictException e) {
        message.fail(409, new FailureDTO(ERROR_PLAYER_REGISTRATION).toJsonString());
    }

    public void handleLobbyFull(Message message) {
         message.fail(400, new FailureDTO(ERROR_CANNOT_ADD_PLAYER_TO_GAME).toJsonString());
    }

    public void handleUnauthorizedPlayer(Message message) {
        message.fail(400, new FailureDTO(ERROR_UNAUTHORIZED_PLAYER).toJsonString());
    }

    public void handleInvalidMove(Message message) {
        message.fail(400, new FailureDTO(ERROR_INVALID_MOVE).toJsonString());
    }

    public void handleInvalidGameCreation(Message message) {
        message.fail(409, new FailureDTO(ERROR_CANNOT_CREATE_GAME).toJsonString());
    }
}
