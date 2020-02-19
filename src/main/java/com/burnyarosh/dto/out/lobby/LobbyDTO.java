package com.burnyarosh.dto.out.lobby;

import com.burnyarosh.dto.IDTO;

public class LobbyDTO implements IDTO {
    private String gameGUID;
    private String player1;

    public LobbyDTO(String guid, String player1) {
        this.gameGUID = guid;
        this.player1 = player1;
    }
    public String getGameGUID() {
        return this.gameGUID;
    }

    public String getPlayer1() {
        return this.player1;
    }
}
