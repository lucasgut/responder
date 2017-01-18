package com.temenos.responder.scaffold;

import com.temenos.responder.entity.runtime.Type;

/**
 * This {@link Scaffold scaffold} validates error messages returned by external applications.
 *
 * @author Andres Burgos
 */
public class ScaffoldExternalErrorMessage implements Scaffold {
    /**
     * An integer to uniquely identify the error code.
     */
    public static final String ERROR_CODE = "ERROR.CODE";
    public static final Type ERROR_CODE_TYPE = Type.INTEGER;
    /**
     * The error's description text.
     */
    public static final String ERROR_TEXT = "ERROR.TEXT";
    public static final Type ERROR_TEXT_TYPE = Type.STRING;
    /**
     * Additional error information.
     */
    public static final String ERROR_INFO = "ERROR.INFO";
    public static final Type ERROR_INFO_TYPE = Type.STRING;
}
