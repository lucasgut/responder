package com.temenos.responder.commands

import static com.temenos.responder.scaffold.Scaffolds.fromArray

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.scaffold.ScaffoldExternalAddress

/**
 * Created by dgroves on 19/01/2017.
 */
class ExternalCustomerAddress implements Command {

    @Override
    void execute(CommandContext context) {
        Entity entity = new Entity();
        if (context.from()[0] == "1") {
            entity.set(fromArray(ScaffoldExternalAddress.STREET_ITEM_STREET,0), "Station Road");
            entity.set(fromArray(ScaffoldExternalAddress.STREET_ITEM_STREET,1), "Dustbin Road");
            entity.set(fromArray(ScaffoldExternalAddress.ADDRESS_ITEM_ADDRESS_ITEM_ADDRESS,0,0), "1 Station Road");
            entity.set(fromArray(ScaffoldExternalAddress.ADDRESS_ITEM_ADDRESS_ITEM_ADDRESS,0,1), "321 Dustbin Road");
            entity.set(fromArray(ScaffoldExternalAddress.TOWN_COUNTRY_ITEM_TOWN_COUNTRY, 0), "Hitchin, GB");
            entity.set(fromArray(ScaffoldExternalAddress.TOWN_COUNTRY_ITEM_TOWN_COUNTRY, 1), "Teddington, GB");
            entity.set(fromArray(ScaffoldExternalAddress.POST_CODE_ITEM_POST_CODE, 0), "AL5 2TH");
            entity.set(fromArray(ScaffoldExternalAddress.POST_CODE_ITEM_POST_CODE, 1), "TW11 0AZ");
            entity.set(fromArray(ScaffoldExternalAddress.COUNTRY_ITEM_COUNTRY, 0), "GB");
            entity.set(fromArray(ScaffoldExternalAddress.COUNTRY_ITEM_COUNTRY, 1), "GB");
            entity.set(fromArray(ScaffoldExternalAddress.PHONE_1_ITEM_PHONE_1, 0), "0");
            entity.set(fromArray(ScaffoldExternalAddress.PHONE_1_ITEM_PHONE_1, 1), "0");
            entity.set(fromArray(ScaffoldExternalAddress.SMS_1_ITEM_SMS_1, 0), "1");
            entity.set(fromArray(ScaffoldExternalAddress.EMAIL_1_ITEM_EMAIL_1, 0), "foo@bar.com");
            entity.set(fromArray(ScaffoldExternalAddress.OFF_PHONE_ITEM_OFF_PHONE, 0), "2");
            entity.set(fromArray(ScaffoldExternalAddress.FAX_1_ITEM_FAX_1, 0), "3");
            entity.set(fromArray(ScaffoldExternalAddress.SECURE_MESSAGE_ITEM_SECURE_MESSAGE, 0), "Hello, World!");
            context.setAttribute(context.into(), entity);
        } else {
            entity.set(fromArray(ScaffoldExternalAddress.STREET_ITEM_STREET,0), "Snake Pass");
            entity.set(fromArray(ScaffoldExternalAddress.ADDRESS_ITEM_ADDRESS_ITEM_ADDRESS,0,0), "30 Snake Pass");
            entity.set(fromArray(ScaffoldExternalAddress.TOWN_COUNTRY_ITEM_TOWN_COUNTRY, 0), "Sheffield, GB");
            entity.set(fromArray(ScaffoldExternalAddress.POST_CODE_ITEM_POST_CODE, 0), "S1 2BP");
            entity.set(fromArray(ScaffoldExternalAddress.COUNTRY_ITEM_COUNTRY, 0), "GB");
            entity.set(fromArray(ScaffoldExternalAddress.PHONE_1_ITEM_PHONE_1, 0), "0");
            entity.set(fromArray(ScaffoldExternalAddress.SMS_1_ITEM_SMS_1, 0), "1");
            entity.set(fromArray(ScaffoldExternalAddress.EMAIL_1_ITEM_EMAIL_1, 0), "foo@bar.com");
            entity.set(fromArray(ScaffoldExternalAddress.OFF_PHONE_ITEM_OFF_PHONE, 0), "2");
            entity.set(fromArray(ScaffoldExternalAddress.FAX_1_ITEM_FAX_1, 0), "3");
            entity.set(fromArray(ScaffoldExternalAddress.SECURE_MESSAGE_ITEM_SECURE_MESSAGE, 0), "Hello, World!");
            context.setAttribute(context.into(), entity);
        }
    }
}
