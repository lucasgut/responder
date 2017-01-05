package com.temenos.responder.commands;

import com.google.inject.Singleton;
import com.temenos.responder.context.CommandContext;
import com.temenos.responder.context.ExecutionContext;

/**
 * Created by Douglas Groves on 09/12/2016.
 */
@Singleton
public interface Command {
    void execute(CommandContext context);
}