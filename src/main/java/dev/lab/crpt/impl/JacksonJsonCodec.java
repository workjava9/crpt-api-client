package dev.lab.crpt.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lab.crpt.core.JsonCodec;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JacksonJsonCodec implements JsonCodec {

    private final ObjectMapper mapper;


    @Override
    public String toJson(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON serialization failed", e);
        }
    }
}
