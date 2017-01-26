package com.temenos.responder.scaffold.dashboard;

import com.temenos.responder.entity.runtime.Type;
import com.temenos.responder.scaffold.Scaffold;

/**
 * @author Andres Burgos
 */
public class ScaffoldT24CustomerInformation implements Scaffold {
    /**
     * An integer to uniquely identify a customer.
     */
    public static final String ID = "ID";
    public static final Type ID_TYPE = Type.INTEGER;
    /**
     * The customer's full name.
     */
    public static final String NAME = "NAME";
    public static final Type NAME_TYPE = Type.STRING;
    /**
     * The customer's home address.
     */
    public static final String HOME_ADDRESS = "HOME\\.ADDRESS";
    public static final Type HOME_ADDRESS_TYPE = Type.OBJECT;
    /**
     * The home address' first line.
     */
    public static final String HOME_ADDRESS_LINE_1 = "LINE1";
    public static final Type HOME_ADDRESS_LINE_1_TYPE = Type.STRING;
    /**
     * The home address' second line.
     */
    public static final String HOME_ADDRESS_LINE_2 = "LINE2";
    public static final Type HOME_ADDRESS_LINE_2_TYPE = Type.STRING;
    /**
     * The home address' post code.
     */
    public static final String HOME_ADDRESS_POST_CODE = "POSTCODE";
    public static final Type HOME_ADDRESS_POST_CODE_TYPE = Type.STRING;
    /**
     * The customer's work address.
     */
    public static final String WORK_ADDRESS = "WORK\\.ADDRESS";
    public static final Type WORK_ADDRESS_TYPE = Type.OBJECT;
    /**
     * The work address' first line.
     */
    public static final String WORK_ADDRESS_LINE_1 = "LINE1";
    public static final Type WORK_ADDRESS_LINE_1_TYPE = Type.STRING;
    /**
     * The work address' second line.
     */
    public static final String WORK_ADDRESS_LINE_2 = "LINE2";
    public static final Type WORK_ADDRESS_LINE_2_TYPE = Type.STRING;
    /**
     * The work address' post code.
     */
    public static final String WORK_ADDRESS_POST_CODE = "POSTCODE";
    public static final Type WORK_ADDRESS_POST_CODE_TYPE = Type.STRING;
    /**
     * The customer's relatives.
     */
    public static final String RELATIVES = "RELATIVES";
    public static final Type RELATIVES_TYPE = Type.ARRAY;
    /**
     * The relative's name.
     */
    public static final String RELATIVES_NAME = "NAME";
    public static final Type RELATIVES_NAME_TYPE = Type.STRING;
    /**
     * The relative's relationship with the customer.
     */
    public static final String RELATIVES_RELATIONSHIP = "RELATIONSHIP";
    public static final Type RELATIVES_RELATIONSHIP_TYPE = Type.STRING;
    /**
     * The customer's accounts.
     */
    public static final String ACCOUNTS = "ACCOUNTS";
    public static final Type ACCOUNTS_TYPE = Type.ARRAY;
}
