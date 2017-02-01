package com.temenos.responder.adapter;

import com.temenos.responder.entity.runtime.Entity;

public interface Adapter {
    <P extends AdapterParameters> Entity execute(P parameters);
}
