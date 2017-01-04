package com.temenos.responder.scaffold

import com.temenos.responder.entity.runtime.Type

/**
 * Created by Douglas Groves on 20/12/2016.
 */
enum ScaffoldAdditionOutput implements Scaffold {
    RESULT('result', Type.INTEGER);

    final String name
    final Type type

    private ScaffoldAdditionOutput(final String name, final Type type){
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