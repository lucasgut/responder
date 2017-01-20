package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity

/**
 * Created by dgroves on 19/01/2017.
 */
class ExternalCustomerAddress implements Command {

    @Override
    void execute(CommandContext context) {
        if (context.from()[0] == "1") {
            context.setAttribute(context.into(), new Entity([
                    "STREET"        : [["STREET": "Station Road"], ["STREET": "Dustbin Road"]],
                    "ADDRESS"       : [["ADDRESS": [["ADDRESS": "1 Station Road"], ["ADDRESS": "321 Dustbin Road"]]]],
                    "TOWN.COUNTRY"  : [["TOWN.COUNTRY": "Hitchin, GB"], ["TOWN.COUNTRY": "Teddington, GB"]],
                    "POST.CODE"     : [["POST.CODE": "AL5 2TH"], ["POST.CODE": "TW11 0AZ"]],
                    "COUNTRY"       : [["COUNTRY": "GB"], ["COUNTRY": "GB"]],
                    "PHONE.1"       : [["PHONE.1": "0"], ["PHONE.1": "0"]],
                    "SMS.1"         : [["SMS.1": "1"]],
                    "EMAIL.1"       : [["EMAIL.1": "foo@bar.com"]],
                    "OFF.PHONE"     : [["OFF.PHONE": "2"]],
                    "FAX.1"         : [["FAX.1": "3"]],
                    "SECURE.MESSAGE": [["SECURE.MESSAGE": "Hello, World!"]]
            ]))
        } else {
            context.setAttribute(context.into(), new Entity([
                    "STREET"        : [["STREET": "Snake Pass"]],
                    "ADDRESS"       : [["ADDRESS": [["ADDRESS": "30 Snake Pass"]]]],
                    "TOWN.COUNTRY"  : [["TOWN.COUNTRY": "Sheffield, GB"]],
                    "POST.CODE"     : [["POST.CODE": "S1 2BP"]],
                    "COUNTRY"       : [["COUNTRY": "GB"]],
                    "PHONE.1"       : [["PHONE.1": "0"]],
                    "SMS.1"         : [["SMS.1": "1"]],
                    "EMAIL.1"       : [["EMAIL.1": "foo@bar.com"]],
                    "OFF.PHONE"     : [["OFF.PHONE": "2"]],
                    "FAX.1"         : [["FAX.1": "3"]],
                    "SECURE.MESSAGE": [["SECURE.MESSAGE": "Hello, World!"]]
            ]))
        }
    }
}
