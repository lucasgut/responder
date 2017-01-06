package com.temenos.responder.conditions

import spock.lang.Specification

/**
 * Created by aburgos on 06/01/2017.
 */
class EqualOperatorTest extends Specification {
    def "Apply"(lh, rh, expected) {
        setup:
            def operator = new EqualOperator<>()
        when:
            def eval = operator.apply(lh, rh)
        then:
            eval == expected
        where:
            lh    | rh      | expected
            0     | 0       | true
            0     | 1       | false
            "hi"  | "there" | false
            "hi"  | "hi"    | true
            true  | true    | true
            true  | false   | false
            false | _       | false
            _     | false   | false
    }
}
