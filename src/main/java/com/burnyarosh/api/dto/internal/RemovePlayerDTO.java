package com.burnyarosh.api.dto.internal;

import com.burnyarosh.api.dto.IDTO;

public class RemovePlayerDTO implements IDTO {
    private String playerGUID;

    public String getPlayerGUID() {
        return this.playerGUID;
    }

    public void setPlayerGUID(String playerGUID) {
        this.playerGUID = playerGUID;
    }
}
