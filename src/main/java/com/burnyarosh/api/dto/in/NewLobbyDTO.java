package com.burnyarosh.api.dto.in;

public class NewLobbyDTO extends AbstractRequestDTO {
    String playerGUID;

    public String getPlayerGUID() {
        return playerGUID;
    }

    public void setPlayerGUID(String playerGUID) {
        this.playerGUID = playerGUID;
    }

    @Override
    public boolean isValidRequest() {
        return this.playerGUID != null;
    }
}
