package com.temenos.responder.context.builder;

import com.temenos.responder.context.Context;

/**
 * Created by dgroves on 31/01/2017.
 */
public interface ContextBuilder<T extends Context> {
    T build();
    long buildAndGetId();
}
