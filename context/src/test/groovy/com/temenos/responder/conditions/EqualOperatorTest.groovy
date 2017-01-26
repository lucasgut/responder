package com.temenos.responder.conditions

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 06/01/2017.
 */
class EqualOperatorTest extends Specification {

    @Unroll
    def "#test evaluates to #expected"(lh, rh, expected, test) {
        setup:
            def operator = new EqualOperator<>()
        when:
            def eval = operator.apply(lh, rh)
        then:
            eval == expected
        where:
            lh   | rh      | expected | test
            0    | 0       | true     | '0 == 0'
            0    | 1       | false    | '0 == 1'
            "hi" | "there" | false    | 'hi == there'
            "hi" | "hi"    | true     | 'hi == hi'
            true | true    | true     | 'true == true'
            true | false   | false    | 'true == false'
    }
}
