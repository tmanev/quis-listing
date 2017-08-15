package com.manev.quislisting.service.post.dto.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Timestamp;

public class TimestampDeserializer extends JsonDeserializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        return new Timestamp(jp.getLongValue());
    }

}