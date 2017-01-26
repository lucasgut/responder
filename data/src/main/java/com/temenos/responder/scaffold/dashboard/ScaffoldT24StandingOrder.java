package com.temenos.responder.scaffold.dashboard;

import com.temenos.responder.entity.runtime.Type;
import com.temenos.responder.scaffold.Scaffold;

/**
 * @author Andres Burgos
 */
public class ScaffoldT24StandingOrder implements Scaffold {
    /**
     * An integer to uniquely identify a standing order.
     */
    public static final String ID = "ID";
    public static final Type ID_TYPE = Type.INTEGER;
    /**
     * The standing order's target account.
     */
    public static final String T24_TARGET_ACCOUNT = "TARGET\\.ACCOUNT";
    public static final Type T24_TARGET_ACCOUNT_TYPE = Type.NUMBER;
    /**
     * The standing order's amount.
     */
    public static final String T24_AMOUNT = "AMOUNT";
    public static final Type T24_AMOUNT_TYPE = Type.NUMBER;
    /**
     * The standing order's transaction date.
     */
    public static final String T24_TRANSACTION_DATE = "TRANSACTION\\.DATE";
    public static final Type T24_TRANSACTION_DATE_TYPE = Type.STRING;
}
