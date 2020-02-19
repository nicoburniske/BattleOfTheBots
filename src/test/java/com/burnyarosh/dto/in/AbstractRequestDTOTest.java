package com.burnyarosh.dto.in;

import io.vertx.core.json.JsonObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AbstractRequestDTOTest {

    JsonObject validNewPlayerJson, invalidNewPlayerJson;
    NewPlayerDTO validNewPlayerObject;

    @Before
    public void init() {
        this.validNewPlayerJson =  new JsonObject();
        //this.validNewPlayerJson.put("type", "new_player");
        this.validNewPlayerJson.put("username", "a_username");
        this.validNewPlayerObject = new NewPlayerDTO();
        this.validNewPlayerObject.setUsername("a_username");
    }

    @Test
    public void isValidRequest() {
        // JsonObject test = new JsonObject(Json.encode(new NewPlayerDTO("ausername")));
        NewPlayerDTO dto = this.validNewPlayerJson.mapTo(com.burnyarosh.dto.in.NewPlayerDTO.class);
        assertTrue(dto.equals(validNewPlayerObject));
    }
}