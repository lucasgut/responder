package com.temenos.responder.scaffold.dashboard;

import com.temenos.responder.entity.runtime.Type;
import com.temenos.responder.scaffold.Scaffold;

/**
 * @author Andres Burgos
 */
public class T24CustomerInformation implements Scaffold {
    /**
     * An integer to uniquely identify a customer.
     */
    public static final String ID = "ID";
    public static final Type ID_TYPE = Type.NUMBER;
    /**
     * The customer's full name.
     */
    public static final String NAME = "NAME";
    public static final Type NAME_TYPE = Type.STRING;
    /**
     * The customer's home address.
     */
    public static final String HOME_ADDRESS = "HOME\\.ADDRESS";
    public static final Type HOME_ADDRESS_TYPE = Type.STRING;
    /**
     * The customer's work address.
     */
    public static final String WORK_ADDRESS = "WORK\\.ADDRESS";
    public static final Type WORK_ADDRESS_TYPE = Type.STRING;
    /**
     * The customer's relatives.
     */
    public static final String RELATIVES = "RELATIVES";
    public static final Type RELATIVES_TYPE = Type.OBJECT;
    /**
     * The customer's accounts.
     */
    public static final String ACCOUNTS = "ACCOUNTS";
    public static final Type ACCOUNTS_TYPE = Type.ARRAY;
}
