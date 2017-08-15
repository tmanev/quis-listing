package com.manev.quislisting.service.post.dto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;

public class TimestampSerializer extends JsonSerializer<Timestamp> {

    @Override
    public void serialize(Timestamp value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeNumber(value.getTime());
    }


    @Override
    public Class<Timestamp> handledType() {
        return Timestamp.class;
    }
}
