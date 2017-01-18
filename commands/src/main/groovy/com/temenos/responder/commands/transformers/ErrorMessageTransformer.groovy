package com.temenos.responder.commands.transformers

import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.scaffold.ScaffoldErrorMessage
import com.temenos.responder.scaffold.ScaffoldExternalErrorMessage

import javax.ws.rs.core.Response

/**
 * Converts an entity whose data structure conforms to
 * {@link ScaffoldExternalErrorMessage} into an {@link Entity entity}
 * conforming to {@link com.temenos.responder.scaffold.ScaffoldErrorMessage}.
 *
 * @author Andres Burgos
 * @date 18/01/2017
 */
public class ErrorMessageTransformer implements Command {

    @Override
    void execute(CommandContext context) {
        def fromDirective = context.from()[0]
        def intoDirective = context.into()

        //fetch entity from command context
        def entity = context.getAttribute(fromDirective) as Entity

        // transform external customer model into internal customer model
        Entity responseBody = new Entity([
                (ScaffoldErrorMessage.ERROR_CODE)    : entity.get(ScaffoldExternalErrorMessage.ERROR_CODE),
                (ScaffoldErrorMessage.ERROR_TEXT)    : entity.get(ScaffoldExternalErrorMessage.ERROR_TEXT),
                (ScaffoldErrorMessage.ERROR_INFO)    : entity.get(ScaffoldExternalErrorMessage.ERROR_INFO)
        ])

        //construct response
        context.setResponseCode(Response.Status.OK.statusCode as String)
        context.setAttribute(intoDirective, responseBody)
    }
}
