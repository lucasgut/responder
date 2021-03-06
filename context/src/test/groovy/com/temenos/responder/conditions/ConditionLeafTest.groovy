package com.temenos.responder.conditions

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 06/01/2017.
 */
class ConditionLeafTest extends Specification {

    @Unroll
    def "#test evaluates to #expected"(lh, op, rh, expected, test) {
        setup:
            Condition condition = new ConditionLeaf<>(lh, op, rh)
        when:
            def eval = condition.evaluate()
        then:
            eval == expected
        where:
            lh   | op                 | rh      | expected | test
            0    | { a, b -> a == b } | 0       | true     | '0 == 0'
            0    | { a, b -> a == b } | 1       | false    | '0 == 1'
            0    | { a, b -> a != b } | 0       | false    | '0 != 0'
            0    | { a, b -> a != b } | 1       | true     | '0 != 1'
            "hi" | { a, b -> a != b } | "there" | true     | 'hi != there'
            "hi" | { a, b -> a != b } | "hi"    | false    | 'hi != hi'
    }
}
