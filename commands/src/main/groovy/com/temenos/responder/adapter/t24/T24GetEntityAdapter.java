package com.temenos.responder.adapter.t24;

import com.temenos.responder.adapter.Adapter;
import com.temenos.responder.adapter.AdapterClient;
import com.temenos.responder.entity.runtime.Entity;

public class T24GetEntityAdapter implements Adapter {
    private static T24GetEntityAdapterClient.T24GetEntityAdapterClientBuilder requestBuilder = T24GetEntityAdapterClient.builder();

    public static T24GetEntityAdapterClient.T24GetEntityAdapterClientBuilder requestBuilder() {
        return requestBuilder;
    }

    @Override
    public <P extends AdapterClient> Entity execute(P parameters) {
        return null;
    }
}
