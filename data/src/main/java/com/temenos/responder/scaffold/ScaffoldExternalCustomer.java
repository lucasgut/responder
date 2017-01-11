package com.temenos.responder.scaffold;

import com.temenos.responder.entity.runtime.Type;

/**
 * This {@link Scaffold scaffold} validates response payloads returned by an external resource.
 *
 * @author Douglas Groves
 * @author Andres Burgos
 */
public class ScaffoldExternalCustomer implements Scaffold {
    /**
     * An integer to uniquely identify a customer.
     */
    public static final String CUSTOMER_ID = "CUSTOMER_ID";
    public static final Type CUSTOMER_ID_TYPE = Type.NUMBER;
    /**
     * The customer's full name.
     */
    public static final String CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final Type CUSTOMER_NAME_TYPE = Type.STRING;
    /**
     * The customer's address.
     */
    public static final String CUSTOMER_ADDRESS = "CUSTOMER_ADDRESS";
    public static final Type CUSTOMER_ADDRESS_TYPE = Type.STRING;
}
