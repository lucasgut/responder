package com.temenos.responder.context;

import com.temenos.responder.entity.runtime.Entity;
import lombok.Data;

import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FlowResult {
    private Entity entity;
    private List<Link> links;
    private Map<String, Object> attributes;
    private int status;
    private String errorMessage;

    @java.beans.ConstructorProperties({"entity", "attributes", "status", "errorMessage"})
    protected FlowResult(Entity entity, Map<String, Object> attributes, int status, String errorMessage) {
        this.entity = entity;
        this.attributes = attributes != null ? attributes : new HashMap<>();
        this.status = status;
        this.errorMessage = errorMessage;
    }

    public static FlowResultBuilder builder() {
        return new FlowResultBuilder();
    }

    public boolean isSuccess() {
        return Response.Status.fromStatusCode(status).getFamily() == Response.Status.Family.SUCCESSFUL;
    }

    public static class FlowResultBuilder {
        private Entity entity;
        private Map<String, Object> attributes = new HashMap<>();
        private int status;
        private String errorMessage;

        FlowResultBuilder() {
        }

        public FlowResult.FlowResultBuilder entity(Entity entity) {
            this.entity = entity;
            return this;
        }

        public FlowResult.FlowResultBuilder attribute(String name, Object value) {
            attributes.put(name, value);
            return this;
        }

        public FlowResult.FlowResultBuilder status(int status) {
            this.status = status;
            return this;
        }

        public FlowResult.FlowResultBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public FlowResult build() {
            return new FlowResult(entity, attributes, status, errorMessage);
        }

        public String toString() {
            return "com.temenos.responder.context.FlowResult.FlowResultBuilder(entity=" + this.entity + ", attributes=" + this.attributes + ", status=" + this.status + ", errorMessage=" + this.errorMessage + ")";
        }
    }
}
