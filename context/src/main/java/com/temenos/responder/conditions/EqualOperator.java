package com.temenos.responder.conditions;

/**
 * The EqualOperator class tests two arguments for equality.
 *
 * @author Andres Burgos
 */
public class EqualOperator<T> implements BooleanBinaryOperator<T> {
    @Override
    public Boolean apply(T lh, T rh) {
        return lh.equals(rh);
    }
}
