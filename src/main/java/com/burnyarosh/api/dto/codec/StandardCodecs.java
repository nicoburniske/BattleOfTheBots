package com.burnyarosh.api.dto.codec;


import com.burnyarosh.api.dto.in.*;
import com.burnyarosh.api.dto.internal.PlayerUpdateDTO;
import com.burnyarosh.api.dto.internal.RemovePlayerDTO;

import java.util.ArrayList;
import java.util.List;

public class StandardCodecs {
    public static List<DTOCodec> getStandardCodecs() {
        List<DTOCodec> codecs = new ArrayList();

        codecs.add(new DTOCodec<>(JoinLobbyDTO.class));
        codecs.add(new DTOCodec<>(NewPlayerDTO.class));
        codecs.add(new DTOCodec<>(NewLobbyDTO.class));
        codecs.add(new DTOCodec<>(PlayerTurnDTO.class));
        codecs.add(new DTOCodec<>(RequestDTOImpl.class));
        codecs.add(new DTOCodec<>(PlayerUpdateDTO.class));
        codecs.add(new DTOCodec<>(RemovePlayerDTO.class));

        return codecs;
    }
}
