package com.temenos.responder.conditions

import spock.lang.Specification

/**
 * Created by aburgos on 06/01/2017.
 */
class ConditionCompositeTest extends Specification {
    def "Evaluate"(conditions, expected) {
        setup:
            Condition condition = new ConditionComposite(conditions)
        when:
            def eval = condition.evaluate()
        then:
            eval == expected
        where:
            conditions      | expected
            []              | true
            [ new ConditionLeaf<>(0, {a, b -> a == b}, 0) ] | true
            [ new ConditionLeaf<>(0, {a, b -> a == b}, 1) ] | false
            [ new ConditionLeaf<>(0, {a, b -> a == b}, 0), new ConditionLeaf<>(1, {a, b -> a == b}, 1) ] | true
            [ new ConditionLeaf<>(0, {a, b -> a == b}, 0), new ConditionLeaf<>(0, {a, b -> a == b}, 1) ] | false
            [ new ConditionLeaf<>(0, {a, b -> a != b}, 0), _ ] | false
            [ new ConditionLeaf<>(0, {a, b -> a != b}, 0), _, _ ] | false
    }
}
