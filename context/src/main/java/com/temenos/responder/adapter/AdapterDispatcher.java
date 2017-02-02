package com.temenos.responder.adapter;

import com.google.inject.Singleton;
import com.temenos.responder.context.FlowException;
import com.temenos.responder.context.FlowResult;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.Flow;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class AdapterDispatcher {
    private final Map<AdapterIdentifier, Adapter> adapters = new HashMap<>();

    public <T extends AdapterParameters> AdapterResult invokeAdapter(AdapterIdentifier adapterId, T adapterParameters) throws AdapterException {
        try {
            Entity entity = adapters.get(adapterId).execute(adapterParameters);
            return AdapterResult.builder()
                    .entity(entity)
                    .build();
        } catch (AdapterException e) {
            return AdapterResult.builder()
                    .errorCode(e.getCode())
                    .errorMessage(e.getMessage())
                    .build();
        } catch (Exception e) {
            return AdapterResult.builder()
                    .errorMessage(e.getMessage())
                    .build();
        }
    }
}
