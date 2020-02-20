package com.burnyarosh.api.processor;

import com.burnyarosh.api.dto.out.FailureDTO;
import com.burnyarosh.api.exception.lobby.NewPlayerConflictException;
import io.vertx.core.eventbus.Message;

public class MessageFailureHandler {

    public void handleNewPlayerConflict(Message message, NewPlayerConflictException e) {
        message.fail(409, new FailureDTO("Error: Unable to register player").toJsonString());
    }

    public void handleLobbyFull(Message message) {
         message.fail(400, new FailureDTO("Error: Cannot join game").toJsonString());
    }

    public void handleUnauthorizedPlayer(Message message) {
        message.fail(400, new FailureDTO("Error: Unauthorized user").toJsonString());
    }

    public void handleInvalidMove(Message message) {
        message.fail(400, new FailureDTO("Error: Invalid Move").toJsonString());
    }
}
