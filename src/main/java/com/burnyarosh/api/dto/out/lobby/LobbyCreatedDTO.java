package com.burnyarosh.api.dto.out.lobby;

import com.burnyarosh.api.dto.IDTO;

public class LobbyCreatedDTO implements IDTO {
    private String gameGUID;

    public LobbyCreatedDTO(String gameGUID) {
        this.gameGUID = gameGUID;
    }

    public String getGameGUID() {
        return gameGUID;
    }

    public void setGameGUID(String gameGUID) {
        this.gameGUID = gameGUID;
    }
}
