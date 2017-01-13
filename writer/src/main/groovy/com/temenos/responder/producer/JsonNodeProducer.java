package com.temenos.responder.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by aburgos on 13/01/2017.
 */
public class JsonNodeProducer implements Producer<JsonNode, String> {
    @Override
    public JsonNode deserialise(String input) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readValue(input, JsonNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return node;
    }

    @Override
    public String serialise(JsonNode input) {
        return null;
    }
}
