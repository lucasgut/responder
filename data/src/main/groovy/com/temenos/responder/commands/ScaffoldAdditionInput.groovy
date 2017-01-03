package com.temenos.responder.commands

import com.temenos.responder.entity.runtime.Type

/**
 * Created by Douglas Groves on 20/12/2016.
 */
enum ScaffoldAdditionInput implements Scaffold {
    OPERANDS('operands', Type.ARRAY);

    final String name
    final Type type

    private ScaffoldAdditionInput(final String name, final Type type){
        this.name = name
        this.type = type
    }

    @Override
    String getName() {
        return name
    }

    @Override
    Type getType(){
        return type
    }
}