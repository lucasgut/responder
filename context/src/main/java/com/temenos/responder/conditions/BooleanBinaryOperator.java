package com.temenos.responder.conditions;

import java.util.function.BiFunction;

/**
 * A class implementing BooleanBinaryOperator accepts two arguments and produces a boolean result based on some
 * test between these arguments.
 *
 * @author Andres Burgos
 */
public interface BooleanBinaryOperator<T> extends BiFunction<T, T, Boolean> {
}
