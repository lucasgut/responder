package com.temenos.responder.adapter.t24;

import com.temenos.responder.adapter.Adapter;
import com.temenos.responder.adapter.AdapterParameters;
import com.temenos.responder.entity.runtime.Entity;

public class T24GetEntityAdapter implements Adapter {
    private static T24GetEntityAdapterParameters.T24GetEntityAdapterParametersBuilder requestBuilder = T24GetEntityAdapterParameters.builder();

    public static T24GetEntityAdapterParameters.T24GetEntityAdapterParametersBuilder requestBuilder() {
        return requestBuilder;
    }

    @Override
    public <P extends AdapterParameters> Entity execute(P parameters) {
        return null;
    }
}
