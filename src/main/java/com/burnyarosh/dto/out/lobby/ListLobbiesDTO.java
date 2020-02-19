package com.burnyarosh.dto.out.lobby;

import com.burnyarosh.dto.IDTO;

import java.util.List;

public class ListLobbiesDTO implements IDTO {
    private List<LobbyDTO> lobbies;

    public ListLobbiesDTO(List<LobbyDTO> lobbies) {
        this.lobbies = lobbies;
    }

    public List<LobbyDTO> getLobbies() {
        return lobbies;
    }

    public void setLobbies(List<LobbyDTO> lobbies) {
        this.lobbies = lobbies;
    }
}
