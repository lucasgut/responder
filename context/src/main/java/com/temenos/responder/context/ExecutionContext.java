package com.temenos.responder.context;

import com.temenos.responder.commands.Command;
import com.temenos.responder.entity.runtime.Entity;

/**
 * Created by Douglas Groves on 13/12/2016.
 */
public interface ExecutionContext extends Context {
    String getSelf();
    String getResourceName();
    Command getCommand(Class<Command> clazz);
    Parameters getParameters();
    Entity getRequestBody();
    void setResponseCode(String code);
    String getResponseCode();
}
