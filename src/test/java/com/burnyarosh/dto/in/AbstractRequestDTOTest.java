package com.burnyarosh.dto.in;

import io.vertx.core.json.JsonObject;
import org.junit.Before;
import org.junit.Test;

public class AbstractRequestDTOTest {

    JsonObject validNewPlayerJson, invalidNewPlayerJson;
    NewPlayerDTO validNewPlayerObject;

    @Before
    public void init() {
        this.validNewPlayerJson =  new JsonObject();
        this.validNewPlayerJson.put("type", "new_player");
        this.validNewPlayerJson.put("username", "ausername");
        this.validNewPlayerObject = new NewPlayerDTO( "ausername");
    }
    @Test
    public void isValidRequest() {

        // JsonObject test = new JsonObject(Json.encode(new NewPlayerDTO("ausername")));
        System.out.println(this.validNewPlayerObject.toJson());
        /*NewPlayerDTO dto = this.validNewPlayerJson.mapTo(NewPlayerDTO.class);
        assertTrue(dto.equals(validNewPlayerObject));*/
    }
}