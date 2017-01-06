package com.temenos.responder.conditions

import spock.lang.Specification

/**
 * Created by aburgos on 06/01/2017.
 */
class ConditionLeafTest extends Specification {
    def "Evaluate"(lh, op, rh, expected) {
        setup:
            Condition condition = new ConditionLeaf<>(lh, op, rh)
        when:
            def eval = condition.evaluate()
        then:
            eval == expected
        where:
            lh   | op                 | rh      | expected
            0    | { a, b -> a == b } | 0       | true
            0    | { a, b -> a == b } | 1       | false
            0    | { a, b -> a != b } | 0       | false
            0    | { a, b -> a != b } | 1       | true
            "hi" | { a, b -> a != b } | "there" | true
            "hi" | { a, b -> a != b } | "hi"    | false
    }
}
