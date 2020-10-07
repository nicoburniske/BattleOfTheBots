package com.burnyarosh.dto.in;

import com.burnyarosh.api.dto.in.JoinLobbyDTO;
import com.burnyarosh.api.dto.in.NewPlayerDTO;
import com.burnyarosh.api.exception.socket.JsonMappingException;
import com.burnyarosh.api.processor.utils.Mapper;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AbstractRequestDTOTest {

    JsonObject validNewPlayerJson, invalidNewPlayerJson;
    NewPlayerDTO validNewPlayerObject;
    JsonObject joinNewLobbyInvalidJson;

    @BeforeEach
    public void init() {
        this.validNewPlayerJson =  new JsonObject();
        this.validNewPlayerJson.put("username", "a_username");
        this.validNewPlayerObject = new NewPlayerDTO();
        this.validNewPlayerObject.setUsername("a_username");

        this.invalidNewPlayerJson = new JsonObject();
        this.invalidNewPlayerJson.put("poop", "crap");
        this.joinNewLobbyInvalidJson = new JsonObject();
        this.joinNewLobbyInvalidJson.put("playerGUID", "aguid");
    }

    @Test
    public void isValidRequest() {
        // JsonObject test = new JsonObject(Json.encode(new NewPlayerDTO("ausername")));
        NewPlayerDTO dto = Mapper.getJsonAsClass(validNewPlayerJson, NewPlayerDTO.class);
        assertTrue(dto.equals(validNewPlayerObject));
    }
    @Test
    public void invalidRequest() {
       Assertions.assertThrows(JsonMappingException.class,  () -> Mapper.getJsonAsClass(invalidNewPlayerJson, NewPlayerDTO.class));
    }
    @Test
    public void invalidRequest2() {
        JoinLobbyDTO dto = Mapper.getJsonAsClass(joinNewLobbyInvalidJson, JoinLobbyDTO.class);
        assertTrue(dto.getGameGUID() == null);
        assertFalse(dto.isValidRequest());
        }
}