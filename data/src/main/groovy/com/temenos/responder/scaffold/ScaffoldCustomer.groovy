package com.temenos.responder.scaffold

import com.temenos.responder.entity.runtime.Type

/**
 * Created by aburgos on 23/12/2016.
 */
enum ScaffoldCustomer implements Scaffold {
    // should be a NUMBER but currently Entity
    // loses the type when constructed

    CUSTOMER_ID('CustomerId', Type.INTEGER),
    CUSTOMER_NAME('CustomerName', Type.STRING),
    CUSTOMER_ADDRESS('CustomerAddress', Type.STRING);

    final String name
    final Type type

    private ScaffoldCustomer(final String name, final Type type){
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