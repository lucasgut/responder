package com.temenos.responder.conditions;

/**
 * Created by aburgos on 05/01/2017.
 */
public class EqualOperator<T> implements BooleanBinaryOperator<T> {
    @Override
    public Boolean apply(T lh, T rh) {
        return lh.equals(rh);
    }
}
