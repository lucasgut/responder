package com.temenos.responder.entity.runtime;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Douglas Groves on 04/01/2017.
 */
public class Document {
    private static final String LINKS_ELEM = "_links";
    private static final String EMBED_ELEM = "_embedded";

    private final Entity links;
    private final Entity embedded;
    private final Entity body;
    private final Entity document;
    private final String modelName;

    public Document(Entity links, Entity embedded, Entity body, String modelName){
        this.links = links;
        this.embedded = embedded;
        this.body = body;
        this.modelName = modelName;
        this.document = render();
    }

    private Entity render(){
        Map<String, Object> data = new LinkedHashMap<>();
        data.put(LINKS_ELEM, links.getValues());
        data.put(EMBED_ELEM, embedded.getValues());
        data.put(modelName, body.getValues());
        return new Entity(data);
    }

    public Entity getLinks() {
        return links;
    }

    public Entity getEmbedded() {
        return embedded;
    }

    public Entity getBody() {
        return body;
    }

    public Entity getDocument() {
        return document;
    }
}
