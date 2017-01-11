package com.temenos.responder.conditions;

import java.util.List;

/**
 * This class composes an expression from multiple {@link Condition conditions}.
 *
 * @author Andres Burgos
 */
public class ConditionComposite implements Condition {

    private List<Condition> conditions;

    public ConditionComposite(List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean evaluate() {
        if(conditions.isEmpty()) return true;
        else {
            Condition head = conditions.get(0);
            ConditionComposite tail = new ConditionComposite(conditions.subList(1, conditions.size()));
            return head.evaluate() && tail.evaluate();
        }
    }
}
