package com.temenos.responder.commands

import com.temenos.responder.entity.runtime.Type

/**
 * Created by Douglas Groves on 20/12/2016.
 */
enum ScaffoldVersion implements Scaffold {
    VERSION_NUMBER('versionNumber', Type.NUMBER),
    BUILD_DATE('buildDate', Type.STRING),
    BLAME('blameThisPerson', Type.STRING);

    final String name
    final Type type

    ScaffoldVersion(final String name, final Type type){
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