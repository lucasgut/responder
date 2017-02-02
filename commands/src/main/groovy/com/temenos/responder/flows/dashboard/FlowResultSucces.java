package com.temenos.responder.flows.dashboard;

import com.temenos.responder.context.FlowResult;
import com.temenos.responder.entity.runtime.Entity;

import javax.ws.rs.core.Response;

public class FlowResultSucces extends FlowResult {
    public FlowResultSucces(Entity entity) {
        super(entity, null, Response.Status.OK.getStatusCode(), null);
    }
}
