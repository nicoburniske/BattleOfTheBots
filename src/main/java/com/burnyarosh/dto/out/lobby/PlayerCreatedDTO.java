package com.burnyarosh.dto.out.lobby;

import com.burnyarosh.dto.IDTO;

public class PlayerCreatedDTO implements IDTO {
    String playerGUID;
    String playerName;

    public PlayerCreatedDTO(String playerGUID, String playerName) {
        this.playerGUID = playerGUID;
        this.playerName = playerName;
    }

    public String getPlayerGUID() {
        return playerGUID;
    }

    public void setPlayerGUID(String playerGUID) {
        this.playerGUID = playerGUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}