package com.burnyarosh.dto.out.lobby;

import com.burnyarosh.dto.IDTO;

public class LobbyCreatedDTO implements IDTO {
    String gameGUID;

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
