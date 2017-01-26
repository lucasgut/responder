package com.temenos.responder.scaffold.dashboard;

import com.temenos.responder.entity.runtime.Type;
import com.temenos.responder.scaffold.Scaffold;

/**
 * @author Andres Burgos
 */
public class ScaffoldT24AccountInformation implements Scaffold {
    /**
     * An integer to uniquely identify an account.
     */
    public static final String T24_ID = "ID";
    public static final Type T24_ID_TYPE = Type.INTEGER;
    /**
     * The account's label.
     */
    public static final String T24_LABEL = "LABEL";
    public static final Type T24_LABEL_TYPE = Type.STRING;
    /**
     * The account's number.
     */
    public static final String T24_NUMBER = "NUMBER";
    public static final Type T24_NUMBER_TYPE = Type.STRING;
    /**
     * The account's balance.
     */
    public static final String T24_BALANCE = "BALANCE";
    public static final Type T24_BALANCE_TYPE = Type.NUMBER;
    /**
     * The account's standing orders.
     */
    public static final String T24_STANDING_ORDERS = "STANDING\\.ORDERS";
    public static final Type T24_STANDING_ORDERS_TYPE = Type.ARRAY;
}
