package com.burnyarosh.api.dto.codec;

import com.burnyarosh.api.dto.IDTO;
import com.burnyarosh.api.processor.utils.Mapper;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.json.JsonObject;

public class DTOCodec <T extends IDTO> implements MessageCodec<T, T> {
    private final Class<T> clazz;

    public DTOCodec(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    @Override
    public void encodeToWire(Buffer buffer, T idto) {
        JsonObject json = idto.toJson();

        // Encode object to string
        String jsonToStr = json.encode();

        // Length of JSON: not number of characters
        int length = jsonToStr.getBytes().length;

        // Write data into given buffer
        buffer.appendInt(length);
        buffer.appendString(jsonToStr);
    }

    @Override
    public T decodeFromWire(int i, Buffer buffer) {
        // My custom message starting from this *position* of buffer
        int _pos = i;

        // Length of JSON
        int length = buffer.getInt(_pos);

        // Get JSON string by its length
        // Jump 4 because getInt() == 4 bytes
        String jsonStr = buffer.getString(_pos+=4, _pos+=length);
        JsonObject contentJson = new JsonObject(jsonStr);

        // We can finally create custom message object
        return Mapper.getJsonAsClass(contentJson, this.clazz);
    }

    @Override
    public T transform(T idto) {
        return idto;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName().concat(this.getClazz().getSimpleName());
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
