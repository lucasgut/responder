package com.temenos.responder.configuration

import com.temenos.responder.commands.Command
import com.temenos.responder.context.ExecutionContext;

import java.util.List;

/**
 * Created by Douglas Groves on 13/12/2016.
 */
public class Flow {
    private final List<Command> sequence;

    public Flow(){
        this.sequence = new ArrayList<>();
    }

    public Flow(List<Command> sequence){
        this.sequence = sequence;
    }

    public void execute(ExecutionContext context){

    }
}
