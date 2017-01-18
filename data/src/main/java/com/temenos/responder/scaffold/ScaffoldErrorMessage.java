package com.temenos.responder.scaffold;

import com.temenos.responder.entity.runtime.Type;

/**
 * This {@link Scaffold scaffold} validates error messages.
 *
 * @author Andres Burgos
 */
public class ScaffoldErrorMessage implements Scaffold {
    /**
     * An integer to uniquely identify the error code.
     */
    public static final String ERROR_CODE = "errorCode";
    public static final Type ERROR_CODE_TYPE = Type.INTEGER;
    /**
     * The error's description text.
     */
    public static final String ERROR_TEXT = "errorText";
    public static final Type ERROR_TEXT_TYPE = Type.STRING;
    /**
     * Additional error information.
     */
    public static final String ERROR_INFO = "errorInfo";
    public static final Type ERROR_INFO_TYPE = Type.STRING;
}
