package com.temenos.responder.commands.transformers

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification

/**
 * Created by aburgos on 18/01/2017.
 */
class ErrorMessageTransformerTest extends Specification {
    def "Error message transformer converts external error data format #extnData into internal error data format #intnData"(){
        given:
            def command = new ErrorMessageTransformer()
            def commandContext = Mock(CommandContext)
            def extnCustomer = Mock(Entity)
        when:
            command.execute(commandContext)
        then:
            1 * commandContext.from() >> ['ExtnCustomer']
            1 * commandContext.into() >> 'finalResult'
            1 * commandContext.getAttribute('ExtnCustomer') >> extnCustomer
            1 * extnCustomer.get('ERROR.CODE') >> 404
            1 * extnCustomer.get('ERROR.TEXT') >> 'No customer found'
            1 * extnCustomer.get('ERROR.INFO') >> 'Unknown customer'
            1 * commandContext.setAttribute('finalResult',
                new Entity(['errorCode':404,'errorText':'No customer found','errorInfo':'Unknown customer']))
    }
}
