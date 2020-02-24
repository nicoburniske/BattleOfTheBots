package com.burnyarosh.api.dto.in;

public class JoinLobbyDTO extends AbstractRequestDTO {
    private String playerGUID;
    private String gameGUID;

    public String getPlayerGUID() {
        return playerGUID;
    }

    public void setPlayerGUID(String playerGUID) {
        this.playerGUID = playerGUID;
    }

    public String getGameGUID() {
        return gameGUID;
    }

    public void setGameGUID(String gameGUID) {
        this.gameGUID = gameGUID;
    }

    @Override
    public boolean isValidRequest() {
        return this.playerGUID != null && this.gameGUID != null;
    }

    @Override
    public boolean isAuthorizedPlayer(String playerGUID) {
        return this.playerGUID.equals(playerGUID);
    }
}
