package com.temenos.responder.flows

import static com.temenos.responder.scaffold.Scaffolds.fromArray

import com.temenos.responder.commands.Command
import com.temenos.responder.commands.ExternalCustomerAddress
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.scaffold.ScaffoldAddress
import com.temenos.responder.scaffold.ScaffoldExternalAddress

/**
 * Created by dgroves on 19/01/2017.
 */
class CustomerAddressFlow extends AbstractFlow {

    @Override
    void doExecute(ExecutionContext context) {
        CommandContext commandContext = new DefaultCommandContext()
        commandContext.from([(String) context.getAttribute("AddressId")])
        commandContext.into("finalResult")
        context.getCommand(ExternalCustomerAddress).execute(commandContext)
        Entity output = (Entity) commandContext.getAttribute(commandContext.into())
        def houseNumbersAndRoads = []
        List<?> addresses = output.get(ScaffoldExternalAddress.ADDRESS, List.class)
        for (int i = 0; i < addresses.size(); i++) {
            List<?> addressGroup = output.get(fromArray(ScaffoldExternalAddress.ADDRESS_ITEM_ADDRESS, i), List.class)
            for (int j = 0; j < addressGroup.size(); j++) {
                houseNumbersAndRoads += [
                        "HouseNumber": (output.get(fromArray(ScaffoldExternalAddress.ADDRESS_ITEM_ADDRESS_ITEM_ADDRESS, i, j), String.class) =~ /^([0-9]+).*?$/)[0][1].toInteger(),
                        "Road"       : (output.get(fromArray(ScaffoldExternalAddress.ADDRESS_ITEM_ADDRESS_ITEM_ADDRESS, i, j), String.class) =~ /^\d+?\s(.+?)$/)[0][1]
                ]
            }
        }
        def result = new Entity([
                (ScaffoldAddress.ADDRESS_ID): context.getAttribute("AddressId").toInteger(),
                (ScaffoldAddress.ADDRESSES) : houseNumbersAndRoads
        ])
        context.setAttribute("finalResult", result)
        context.setResponseCode(200)
    }
}
