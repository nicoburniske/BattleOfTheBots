package com.burnyarosh.api.dto.in;

public class RequestDTOImpl extends AbstractRequestDTO {

    private String playerGUID;

    @Override
    public boolean isValidRequest() {
        return true;
    }

    public String getPlayerGUID() {
        return playerGUID;
    }

    public void setPlayerGUID(String playerGUID) {
        this.playerGUID = playerGUID;
    }
}
