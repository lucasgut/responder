package com.temenos.responder.scaffold;

import com.temenos.responder.entity.runtime.Type;

/**
 * This Scaffold validates response payloads returned by the add API.
 *
 * @author Douglas Groves
 */
public class ScaffoldAdditionOutput implements Scaffold {
    public static final String RESULT = "result";
    public static final Type RESULT_TYPE = Type.INTEGER;
}
