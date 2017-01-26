package com.temenos.responder.commands.transformers

import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.scaffold.dashboard.ScaffoldCustomerDashboard_1
import com.temenos.responder.scaffold.dashboard.ScaffoldT24AccountInformation
import com.temenos.responder.scaffold.dashboard.ScaffoldT24CustomerInformation
import com.temenos.responder.scaffold.dashboard.ScaffoldT24StandingOrder

import javax.ws.rs.core.Response

/**
 * Converts an entity whose data structure conforms to
 * {@link ScaffoldT24CustomerInformation} into an {@link Entity entity}
 * conforming to {@link ScaffoldCustomerDashboard_1}.
 *
 * @author Andres Burgos
 */
public class CustomerDashboardTransformer implements Command {

    @Override
    void execute(CommandContext context) {
        def fromDirective = context.getAttribute("from")[0];
        def intoDirective = context.getAttribute("into");

        // fetch entity from command context
        def entity = context.getAttribute(fromDirective) as Entity

        // transform external customer model into internal customer model
        Entity responseBody = new Entity()

        responseBody.set(ScaffoldCustomerDashboard_1.ID, entity.get(ScaffoldT24CustomerInformation.ID))
        responseBody.set(ScaffoldCustomerDashboard_1.NAME, entity.get(ScaffoldT24CustomerInformation.NAME))

        Map<?, ?> homeAddress = new HashMap<>()
        Map<?, ?> t24HomeAddress = entity.get(ScaffoldT24CustomerInformation.HOME_ADDRESS)
        homeAddress.put(ScaffoldCustomerDashboard_1.HOME_ADDRESS_LINE_1, t24HomeAddress.get(ScaffoldT24CustomerInformation.HOME_ADDRESS_LINE_1))
        homeAddress.put(ScaffoldCustomerDashboard_1.HOME_ADDRESS_LINE_2, t24HomeAddress.get(ScaffoldT24CustomerInformation.HOME_ADDRESS_LINE_2))
        homeAddress.put(ScaffoldCustomerDashboard_1.HOME_ADDRESS_POST_CODE, t24HomeAddress.get(ScaffoldT24CustomerInformation.HOME_ADDRESS_POST_CODE))
        responseBody.set(ScaffoldCustomerDashboard_1.HOME_ADDRESS, homeAddress)

        Map<?, ?> workAddress = new HashMap<>()
        Map<?, ?> t24WorkAddress = entity.get(ScaffoldT24CustomerInformation.WORK_ADDRESS)
        workAddress.put(ScaffoldCustomerDashboard_1.WORK_ADDRESS_LINE_1, t24WorkAddress.get(ScaffoldT24CustomerInformation.WORK_ADDRESS_LINE_1))
        workAddress.put(ScaffoldCustomerDashboard_1.WORK_ADDRESS_LINE_2, t24WorkAddress.get(ScaffoldT24CustomerInformation.WORK_ADDRESS_LINE_2))
        workAddress.put(ScaffoldCustomerDashboard_1.WORK_ADDRESS_POST_CODE, t24WorkAddress.get(ScaffoldT24CustomerInformation.WORK_ADDRESS_POST_CODE))
        responseBody.set(ScaffoldCustomerDashboard_1.WORK_ADDRESS, workAddress)

        List<?> relatives = new ArrayList<>()
        List<?> t24AccRelatives = entity.get(ScaffoldT24CustomerInformation.RELATIVES, List.class)
        for(Map<?, ?> t24AccRelative : t24AccRelatives) {
            Map<?, ?> relative = new HashMap<>()
            relative.put(ScaffoldCustomerDashboard_1.RELATIVES_NAME, t24AccRelative.get(ScaffoldT24CustomerInformation.RELATIVES_NAME))
            relative.put(ScaffoldCustomerDashboard_1.RELATIVES_RELATIONSHIP, t24AccRelative.get(ScaffoldT24CustomerInformation.RELATIVES_RELATIONSHIP))
            relatives.add(relative)
        }
        responseBody.set(ScaffoldCustomerDashboard_1.RELATIVES, relatives)

        List<?> accounts = new ArrayList<>()
        List<?> t24Accounts = entity.get(ScaffoldT24CustomerInformation.ACCOUNTS, List.class)
        for(Entity t24Account : t24Accounts) {
            Map<?, ?> account = new HashMap<>()
            account.put(ScaffoldCustomerDashboard_1.ACCOUNTS_LABEL, t24Account.get(ScaffoldT24AccountInformation.LABEL))
            account.put(ScaffoldCustomerDashboard_1.ACCOUNTS_NUMBER, t24Account.get(ScaffoldT24AccountInformation.NUMBER))
            account.put(ScaffoldCustomerDashboard_1.ACCOUNTS_BALANCE, t24Account.get(ScaffoldT24AccountInformation.BALANCE))

            List<?> orders = new ArrayList<>()
            List<?> t24StdOrders = t24Account.get(ScaffoldT24AccountInformation.STANDING_ORDERS, List.class)
            for(Entity t24StdOrder : t24StdOrders) {
                Map<?, ?> order = new HashMap<>()
                order.put(ScaffoldCustomerDashboard_1.ACCOUNTS_STANDING_ORDERS_TARGET, t24StdOrder.get(ScaffoldT24StandingOrder.TARGET_ACCOUNT))
                order.put(ScaffoldCustomerDashboard_1.ACCOUNTS_STANDING_ORDERS_AMOUNT, t24StdOrder.get(ScaffoldT24StandingOrder.AMOUNT))
                orders.add(order)
            }
            account.put(ScaffoldCustomerDashboard_1.ACCOUNTS_STANDING_ORDERS, orders);
            accounts.add(account);
        }
        responseBody.set(ScaffoldCustomerDashboard_1.ACCOUNTS, accounts)

        // construct response
        context.setResponseCode(Response.Status.OK.statusCode as String)
        context.setAttribute(intoDirective, responseBody)
    }
}
