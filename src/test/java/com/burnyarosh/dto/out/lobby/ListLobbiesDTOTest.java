package com.burnyarosh.dto.out.lobby;

import com.burnyarosh.api.dto.out.lobby.ListLobbiesDTO;
import com.burnyarosh.api.dto.out.lobby.LobbyDTO;
import com.burnyarosh.api.dto.entity.Entity;
import com.burnyarosh.api.dto.entity.Game;
import com.burnyarosh.api.dto.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ListLobbiesDTOTest {
    @Test
    public void testToJson() {
        String gameGUID = Entity.generateGUID();
        String playerGuid = Entity.generateGUID();
        Player p1 = new Player("yeet", playerGuid);
        Game g1 = new Game(gameGUID, gameGUID, p1);
        LobbyDTO lobby1 = new LobbyDTO(g1.getId(), g1.getPlayer(1).getName());
        ListLobbiesDTO dto = new ListLobbiesDTO(Arrays.asList(lobby1));
        assertEquals(String.format("{\"lobbies\":[{\"gameGUID\":\"%s\",\"player1\":\"%s\"}]}", gameGUID, p1.getName()), dto.toJsonString());
    }
}