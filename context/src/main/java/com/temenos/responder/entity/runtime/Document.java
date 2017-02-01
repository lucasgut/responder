package com.temenos.responder.entity.runtime;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A document aggregates three {@link Entity entities} - links, embedded content and a body
 * containing data corresponding to a {@link com.temenos.responder.scaffold.Scaffold scaffold definition}.
 *
 * @author Douglas Groves
 */
public class Document {
    private static final String LINKS_ELEM = "_links";
    private static final String EMBED_ELEM = "_embedded";

    private final Entity links;
    private final Entity embedded;
    private final Entity body;
    private final Entity document;
    private final String modelName;
    private final String flowName;

    public Document(Entity links, Entity embedded, Entity body, String modelName, String flowName){
        this.links = links;
        this.embedded = embedded;
        this.body = body;
        this.modelName = modelName;
        this.document = render();
        this.flowName = flowName;
    }

    private Entity render(){
        Map<String, Object> data = new LinkedHashMap<>();
        data.put(LINKS_ELEM, links.getValues());
        if(!embedded.getValues().isEmpty()) {
            data.put(EMBED_ELEM, embedded.getValues());
        }
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

    public String getModelName() {
        return modelName;
    }

    public String getFlowName() {
        return flowName;
    }
}
