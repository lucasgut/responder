package com.temenos.responder.context;

import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.loader.ScriptLoader;
import com.temenos.responder.producer.Producer;

/**
 * Created by Douglas Groves on 13/12/2016.
 */
public interface ExecutionContext extends Context {
    String getSelf();
    Producer getProducer();
    ScriptLoader getScriptLoader();
    Entity getRequestBody();
    void setResponseCode(String code);
    String getResponseCode();
}
