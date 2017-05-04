package com.manev.quislisting.service.post.dto.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * Deserializes java.time.ZonedDateTime from String "yyyy-MM-dd'T'HH:mm:ss'Z'" back to ZonedDateTime.
 */
public class ZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    @Override
    public ZonedDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return ZonedDateTime.parse(jp.getText());
    }

}