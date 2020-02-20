package com.burnyarosh.api.dto.in;

import com.burnyarosh.api.dto.common.CoordDTO;

public class PlayerTurnDTO extends AbstractRequestDTO {
    private String playerGUID;
    private String gameGUID;
    private CoordDTO origin;
    private CoordDTO target;

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

    public CoordDTO getOrigin() {
        return origin;
    }

    public void setOrigin(CoordDTO origin) {
        this.origin = origin;
    }

    public CoordDTO getTarget() {
        return target;
    }

    public void setTarget(CoordDTO target) {
        this.target = target;
    }

    @Override
    public boolean isValidRequest() {
        if (this.playerGUID == null || this.gameGUID == null || this.origin == null || this.target == null) {
            return false;
        } else {
            return true;
        }
    }
}
