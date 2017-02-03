package com.temenos.responder.adapter;

import com.temenos.responder.entity.runtime.Entity;

public interface Adapter {
    <P extends AdapterClient> Entity execute(P parameters) throws AdapterException;
}
