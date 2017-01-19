package com.temenos.responder.scaffold.dashboard;

import com.temenos.responder.entity.runtime.Type;
import com.temenos.responder.scaffold.Scaffold;

/**
 * @author Andres Burgos
 */
public class T24AccountInformation implements Scaffold {
    /**
     * An integer to uniquely identify an account.
     */
    public static final String ID = "ID";
    public static final Type ID_TYPE = Type.NUMBER;
    /**
     * The account's label.
     */
    public static final String LABEL = "LABEL";
    public static final Type LABEL_TYPE = Type.STRING;
    /**
     * The account's number.
     */
    public static final String NUMBER = "NUMBER";
    public static final Type NUMBER_TYPE = Type.STRING;
    /**
     * The account's balance.
     */
    public static final String BALANCE = "BALANCE";
    public static final Type BALANCE_TYPE = Type.NUMBER;
    /**
     * The account's standing orders.
     */
    public static final String STANDING_ORDERS = "STANDING\\.ORDERS";
    public static final Type STANDING_ORDERS_TYPE = Type.ARRAY;
}
