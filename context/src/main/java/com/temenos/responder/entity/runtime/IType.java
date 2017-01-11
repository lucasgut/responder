package com.temenos.responder.entity.runtime;

/**
 * Interface describing getters and setters available to {@link Type type} enumerations.
 *
 * @author Douglas Groves
 */
public interface IType {
    /**
     * Obtain the declared type of a {@link com.temenos.responder.scaffold.Scaffold scaffold field}.
     * @return The declared type of a {@link com.temenos.responder.scaffold.Scaffold scaffold field}.
     */
    String getType();

    /**
     * Obtain an equivalent Java type.
     * @return A Java type equivalent to the declared type.
     */
    Class<?> getStaticType();
}
