package com.temenos.responder.adapter;

public interface AdapterCommand {
    <R extends AdapterResult, P extends AdapterParameters> R execute(final AdapterContext<P> adapterContext) throws AdapterException;
}
