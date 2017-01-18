package com.temenos.responder.scaffold;

import com.temenos.responder.entity.runtime.Type;

/**
 * This {@link Scaffold scaffold} validates response payloads returned by the customer API.
 *
 * @author Douglas Groves
 * @author Andres Burgos
 */
public class ScaffoldCustomerDashboard_1 implements Scaffold {
    /**
     * An integer to uniquely identify a customer.
     */
    public static final String CUSTOMER_ID = "customerId";
    public static final Type CUSTOMER_ID_TYPE = Type.INTEGER;
    /**
     * The customer's full name.
     */
    public static final String CUSTOMER_NAME = "customerName";
    public static final Type CUSTOMER_NAME_TYPE = Type.STRING;
    /**
     * The customer's home address.
     */
    public static final String CUSTOMER_HOME_ADDRESS = "homeAddress";
    public static final Type CUSTOMER_HOME_ADDRESS_TYPE = Type.STRING;
    /**
     * The customer's work address.
     */
    public static final String CUSTOMER_WORK_ADDRESS = "workAddress";
    public static final Type CUSTOMER_WORK_ADDRESS_TYPE = Type.STRING;
}
