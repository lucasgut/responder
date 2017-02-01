package com.temenos.responder.adapter.t24;

import com.temenos.responder.adapter.AdapterIdentifier;

public enum T24AdapterIdentifier implements AdapterIdentifier {
    GetEntity(T24GetEntityAdapter.class);

    private final Class<?> type;

    T24AdapterIdentifier(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
}
