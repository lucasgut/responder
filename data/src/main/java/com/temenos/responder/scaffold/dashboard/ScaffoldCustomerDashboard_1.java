package com.temenos.responder.scaffold.dashboard;

import com.temenos.responder.entity.runtime.Type;
import com.temenos.responder.scaffold.Scaffold;

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
    public static final String ID = "customerId";
    public static final Type ID_TYPE = Type.INTEGER;
    /**
     * The customer's full name.
     */
    public static final String NAME = "customerName";
    public static final Type NAME_TYPE = Type.STRING;
    /**
     * The customer's home address.
     */
    public static final String HOME_ADDRESS = "homeAddress";
    public static final Type HOME_ADDRESS_TYPE = Type.OBJECT;
    /**
     * The home address' first line.
     */
    public static final String HOME_ADDRESS_LINE_1 = "line1";
    public static final Type HOME_ADDRESS_LINE_1_TYPE = Type.STRING;
    /**
     * The home address' second line.
     */
    public static final String HOME_ADDRESS_LINE_2 = "line2";
    public static final Type HOME_ADDRESS_LINE_2_TYPE = Type.STRING;
    /**
     * The home address' post code.
     */
    public static final String HOME_ADDRESS_POST_CODE = "postcode";
    public static final Type HOME_ADDRESS_POST_CODE_TYPE = Type.STRING;
    /**
     * The customer's work address.
     */
    public static final String WORK_ADDRESS = "workAddress";
    public static final Type WORK_ADDRESS_TYPE = Type.OBJECT;
    /**
     * The work address' first line.
     */
    public static final String WORK_ADDRESS_LINE_1 = "line1";
    public static final Type WORK_ADDRESS_LINE_1_TYPE = Type.STRING;
    /**
     * The work address' second line.
     */
    public static final String WORK_ADDRESS_LINE_2 = "line2";
    public static final Type WORK_ADDRESS_LINE_2_TYPE = Type.STRING;
    /**
     * The work address' post code.
     */
    public static final String WORK_ADDRESS_POST_CODE = "postcode";
    public static final Type WORK_ADDRESS_POST_CODE_TYPE = Type.STRING;
    /**
     * The customer's relatives.
     */
    public static final String RELATIVES = "relatives";
    public static final Type RELATIVES_TYPE = Type.ARRAY;
    /**
     * The relative's name.
     */
    public static final String RELATIVES_NAME = "name";
    public static final Type RELATIVES_NAME_TYPE = Type.STRING;
    /**
     * The relative's relationship with the customer.
     */
    public static final String RELATIVES_RELATIONSHIP = "relationship";
    public static final Type RELATIVES_RELATIONSHIP_TYPE = Type.STRING;
    /**
     * The customer's accounts.
     */
    public static final String ACCOUNTS = "accounts";
    public static final Type ACCOUNTS_TYPE = Type.ARRAY;
    /**
     * The account's label.
     */
    public static final String ACCOUNTS_LABEL = "accountLabel";
    public static final Type ACCOUNTS_LABEL_TYPE = Type.STRING;
    /**
     * The account's number.
     */
    public static final String ACCOUNTS_NUMBER = "accountNumber";
    public static final Type ACCOUNTS_NUMBER_TYPE = Type.STRING;
    /**
     * The account's balance.
     */
    public static final String ACCOUNTS_BALANCE = "accountBalance";
    public static final Type ACCOUNTS_BALANCE_TYPE = Type.NUMBER;
    /**
     * The account's standing orders.
     */
    public static final String ACCOUNTS_STANDING_ORDERS = "standingOrders";
    public static final Type ACCOUNTS_STANDING_ORDERS_TYPE = Type.ARRAY;
    /**
     * The standing orders' target account.
     */
    public static final String ACCOUNTS_STANDING_ORDERS_TARGET = "targetAccount";
    public static final Type ACCOUNTS_STANDING_ORDERS_TARGET_TYPE = Type.STRING;
    /**
     * The standing orders' amount.
     */
    public static final String ACCOUNTS_STANDING_ORDERS_AMOUNT = "amount";
    public static final Type ACCOUNTS_STANDING_ORDERS_AMOUNT_TYPE = Type.NUMBER;
}
