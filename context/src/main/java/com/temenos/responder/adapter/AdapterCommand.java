package com.temenos.responder.adapter;

public interface AdapterCommand {
    <R extends AdapterResult> R execute(final AdapterContext adapterContext) throws AdapterException;
}
