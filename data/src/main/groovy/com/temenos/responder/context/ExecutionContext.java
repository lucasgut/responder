package com.temenos.responder.context;

import com.temenos.responder.commands.Command;
import com.temenos.responder.commands.injector.CommandInjector;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.loader.ScriptLoader;
import com.temenos.responder.producer.Producer;

/**
 * Created by Douglas Groves on 13/12/2016.
 */
public interface ExecutionContext extends Context {
    String getSelf();
    Command getCommand(Class<Command> clazz);
    Producer getProducer();
    ScriptLoader getScriptLoader();
    Parameters getParameters();
    Entity getRequestBody();
    void setResponseCode(String code);
    String getResponseCode();
}
