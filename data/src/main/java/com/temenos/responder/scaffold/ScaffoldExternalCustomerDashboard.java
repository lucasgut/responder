package com.temenos.responder.scaffold;

import com.temenos.responder.entity.runtime.Type;

/**
 * This {@link Scaffold scaffold} validates response payloads returned by an external resource.
 *
 * @author Douglas Groves
 * @author Andres Burgos
 */
public class ScaffoldExternalCustomerDashboard implements Scaffold {
    /**
     * An integer to uniquely identify a customer.
     */
    public static final String CUSTOMER_ID = "CUSTOMER\\.ID";
    public static final Type CUSTOMER_ID_TYPE = Type.NUMBER;
    /**
     * The customer's full name.
     */
    public static final String CUSTOMER_NAME = "CUSTOMER\\.NAME";
    public static final Type CUSTOMER_NAME_TYPE = Type.STRING;
    /**
     * The customer's home address.
     */
    public static final String CUSTOMER_HOME_ADDRESS = "CUSTOMER\\.HOME\\.ADDRESS";
    public static final Type CUSTOMER_HOME_ADDRESS_TYPE = Type.STRING;
    /**
     * The customer's work address.
     */
    public static final String CUSTOMER_WORK_ADDRESS = "CUSTOMER\\.WORK\\.ADDRESS";
    public static final Type CUSTOMER_WORK_ADDRESS_TYPE = Type.STRING;
}
