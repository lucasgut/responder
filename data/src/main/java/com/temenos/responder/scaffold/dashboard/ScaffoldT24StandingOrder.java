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
    public static final String TARGET_ACCOUNT = "TARGET\\.ACCOUNT";
    public static final Type TARGET_ACCOUNT_TYPE = Type.NUMBER;
    /**
     * The standing order's amount.
     */
    public static final String AMOUNT = "AMOUNT";
    public static final Type AMOUNT_TYPE = Type.NUMBER;
}
