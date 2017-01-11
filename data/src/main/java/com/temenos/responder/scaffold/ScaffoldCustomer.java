package com.temenos.responder.scaffold;

import com.temenos.responder.entity.runtime.Type;

/**
 * This {@link Scaffold scaffold} validates response payloads returned by the customer API.
 *
 * @author Douglas Groves
 * @author Andres Burgos
 */
public class ScaffoldCustomer implements Scaffold {
    /**
     * An integer to uniquely identify a customer.
     */
    public static final String CUSTOMER_ID = "CustomerId";
    public static final Type CUSTOMER_ID_TYPE = Type.INTEGER;
    /**
     * The customer's full name.
     */
    public static final String CUSTOMER_NAME = "CustomerName";
    public static final Type CUSTOMER_NAME_TYPE = Type.STRING;
    /**
     * The customer's address.
     */
    public static final String CUSTOMER_ADDRESS = "CustomerAddress";
    public static final Type CUSTOMER_ADDRESS_TYPE = Type.STRING;

}
