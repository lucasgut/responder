package com.temenos.responder.adapter;

import com.google.inject.Singleton;
import com.temenos.responder.entity.runtime.Entity;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class AdapterDispatcher {
    private final Map<AdapterIdentifier, Adapter> adapters = new HashMap<>();

    public <T extends AdapterParameters> Entity invokeAdapter(AdapterIdentifier adapterId, T adapterParameters) throws AdapterException {
        try {
            return adapters.get(adapterId).execute(adapterParameters);
        } catch(Exception e) {
            throw new AdapterException(e);
        }

    }
}
