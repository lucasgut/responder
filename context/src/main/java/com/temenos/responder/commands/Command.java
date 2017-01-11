package com.temenos.responder.commands;

import com.google.inject.Singleton;
import com.temenos.responder.context.CommandContext;

import java.util.List;

/**
 * A command is an instruction or action called during flow processing whose inputs and outputs are read from
 * and written to a {@link CommandContext command context}.
 *
 * @author Douglas Groves
 */
@Singleton
public interface Command {

    /**
     * Execute the command.
     *
     * @param context A {@link CommandContext} whose {@link CommandContext#from(List) from}
     * and {@link CommandContext#into(String) into} parameters are mandatory and may
     * have one or more {@link com.temenos.responder.conditions.Condition conditions} attached.
     */
    void execute(CommandContext context);
}