package com.burnyarosh.dto.out.lobby;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class ListPlayersDTOTest {

    @Test
    public void testToJson() {
        ListPlayersDTO dto1 = new ListPlayersDTO(Arrays.asList("player1", "player2", "player3"));
        assertEquals("{\"players\":[\"player1\",\"player2\",\"player3\"]}", dto1.toJson());
    }

}