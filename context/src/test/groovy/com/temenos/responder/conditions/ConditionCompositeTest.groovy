package com.temenos.responder.conditions

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 06/01/2017.
 */
class ConditionCompositeTest extends Specification {
    @Unroll
    def "#test evaluates to #expected"(conditions, expected, test) {
        setup:
            Condition condition = new ConditionComposite(conditions)
        when:
            def eval = condition.evaluate()
        then:
            eval == expected
        where:
            conditions                                                                                     | expected | test
            []                                                                                             | true     | 'true'
            [new ConditionLeaf<>(0, { a, b -> a == b }, 0)]                                                | true     | '0 == 0'
            [new ConditionLeaf<>(0, { a, b -> a == b }, 1)]                                                | false    | '0 == 1'
            [new ConditionLeaf<>(0, { a, b -> a == b }, 0), new ConditionLeaf<>(1, { a, b -> a == b }, 1)] | true     | '0 == 1 && 1 == 1'
            [new ConditionLeaf<>(0, { a, b -> a == b }, 0), new ConditionLeaf<>(0, { a, b -> a == b }, 1)] | false    | '0 == 0 && 0 == 1'
            [new ConditionLeaf<>(0, { a, b -> a != b }, 0)]                                                | false    | '0 != 0'
    }
}
