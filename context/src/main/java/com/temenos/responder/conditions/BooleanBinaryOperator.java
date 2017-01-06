package com.temenos.responder.conditions;

import java.util.function.BiFunction;

/**
 * Created by aburgos on 05/01/2017.
 */
public interface BooleanBinaryOperator<T> extends BiFunction<T, T, Boolean> {
}
