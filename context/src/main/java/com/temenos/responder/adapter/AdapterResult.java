package com.temenos.responder.adapter;

import com.temenos.responder.entity.runtime.Entity;

import java.util.Map;

public interface AdapterResult {
    Entity getEntity();
    Map<String, Object> getAttributes();
}
