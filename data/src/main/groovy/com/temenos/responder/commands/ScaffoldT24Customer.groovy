package com.temenos.responder.commands

import com.temenos.responder.entity.runtime.Type

/**
 * Created by aburgos on 23/12/2016.
 */
enum ScaffoldT24Customer implements Scaffold {
    // should be a NUMBER but currently Entity
    // loses the type when constructed
    CUSTOMER_ID('CUSTOMER_ID', Type.STRING),
    CUSTOMER_NAME('CUSTOMER_NAME', Type.STRING),
    CUSTOMER_ADDRESS('CUSTOMER_ADDRESS', Type.STRING);

    final String name
    final Type type

    private ScaffoldT24Customer(final String name, final Type type){
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