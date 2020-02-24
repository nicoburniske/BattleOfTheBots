package com.burnyarosh.api.dto.codec;


import com.burnyarosh.api.dto.in.*;
import com.burnyarosh.api.dto.internal.PlayerUpdateDTO;

import java.util.ArrayList;
import java.util.List;

public class StandardCodecs {
    public static List<DTOCodec> getStandardCodecs() {
        List<DTOCodec> codecs = new ArrayList();

        codecs.add(new DTOCodec<JoinLobbyDTO>(JoinLobbyDTO.class));
        codecs.add(new DTOCodec<NewPlayerDTO>(NewPlayerDTO.class));
        codecs.add(new DTOCodec<NewLobbyDTO>(NewLobbyDTO.class));
        codecs.add(new DTOCodec<PlayerTurnDTO>(PlayerTurnDTO.class));
        codecs.add(new DTOCodec<RequestDTOImpl>(RequestDTOImpl.class));
        codecs.add(new DTOCodec<PlayerUpdateDTO>(PlayerUpdateDTO.class));

        return codecs;
    }
}
