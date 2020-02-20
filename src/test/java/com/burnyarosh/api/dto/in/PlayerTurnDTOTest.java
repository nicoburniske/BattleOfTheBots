package com.burnyarosh.api.dto.in;

import com.burnyarosh.api.dto.common.CoordDTO;
import com.burnyarosh.api.processor.utils.Mapper;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTurnDTOTest {
    JsonObject playerTurnJson;
    PlayerTurnDTO turn1;
    PlayerTurnDTO turn2;

    @Test
    public void testJSONMapping() {
        JsonObject origin = new JsonObject().put("x", 0).put("y", 50);
        JsonObject target = new JsonObject().put("x", 333).put("y", 50);

        CoordDTO orig = Mapper.getJsonAsClass(origin, CoordDTO.class);
        CoordDTO targ = Mapper.getJsonAsClass(target, CoordDTO.class);

        playerTurnJson = new JsonObject().put("origin", origin).put("target", target);

        turn1 = new PlayerTurnDTO();
        turn1.setOrigin(orig);
        turn1.setTarget(targ);

        turn2 = Mapper.getJsonAsClass(playerTurnJson, PlayerTurnDTO.class);
        System.out.println(turn2.toJsonString());
        System.out.println(turn1.toJsonString());
    }

}