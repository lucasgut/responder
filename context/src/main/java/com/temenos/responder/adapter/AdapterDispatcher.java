package com.temenos.responder.adapter;

import com.google.inject.Singleton;
import com.temenos.responder.context.FlowException;
import com.temenos.responder.entity.runtime.Entity;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class AdapterDispatcher {
    private final Map<AdapterIdentifier, Adapter> adapters = new HashMap<>();

    public <T extends AdapterClient> Entity invokeAdapter(AdapterIdentifier adapterId, T adapterParameters) throws AdapterException {
        try {
            return adapters.get(adapterId).execute(adapterParameters);
        } catch (AdapterException e) {
            throw new FlowException(convertAdapterToFlowErrorCode(e.getFailureType()), e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new FlowException(500, e.getMessage(), null);
        }
    }

    private int convertAdapterToFlowErrorCode(AdapterFailureType failureType) {
        return 0;
    }
}
