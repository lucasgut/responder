package com.temenos.responder.scaffold;

import com.temenos.responder.entity.runtime.Type;

/**
 * This {@link Scaffold scaffold} validates request payloads sent to the add API.
 *
 * @author Douglas Groves
 */
public class ScaffoldAdditionInput implements Scaffold {
    /**
     * A list of integers that will be added together.
     */
    public static final String OPERANDS = "operands";
    public static final Type OPERANDS_TYPE = Type.ARRAY;
}
