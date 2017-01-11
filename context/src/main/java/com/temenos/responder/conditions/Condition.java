package com.temenos.responder.conditions;

/**
 * A condition is an invariant that must evaluate to true if a {@link com.temenos.responder.commands.Command command}
 * is to be executed.
 *
 * @author Andres Burgos
 */
public interface Condition {

    /**
     * Evaluate the condition.
     *
     * @return A boolean result of the evaluation.
     */
    public boolean evaluate();
}
