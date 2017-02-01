package com.temenos.responder.commands.transformers.dashboard;

import com.temenos.responder.commands.Command;
import com.temenos.responder.context.CommandContext;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.scaffold.dashboard.ScaffoldCustomerDashboard_1;
import com.temenos.responder.scaffold.dashboard.ScaffoldT24AccountInformation;
import com.temenos.responder.scaffold.dashboard.ScaffoldT24CustomerInformation;
import com.temenos.responder.scaffold.dashboard.ScaffoldT24StandingOrder;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts a composed entity whose data structure conforms to
 * {@link ScaffoldT24CustomerInformation}, with accounts that conforms to {@link ScaffoldT24AccountInformation},
 * and standing orders that conform to {@link ScaffoldT24StandingOrder}into an {@link Entity entity}
 * conforming to {@link ScaffoldCustomerDashboard_1}.
 *
 * @author Andres Burgos
 */
public class CustomerDashboardTransformer_1 implements Command {

    @Override
    public void execute(CommandContext context) {
        String fromDirective = context.from().get(0);
        String intoDirective = context.into();

        // fetch entity from command context
        Entity entity = (Entity) context.getAttribute(fromDirective);

        // transform external customer model into internal customer model
        Entity responseBody = new Entity();

        responseBody.set(ScaffoldCustomerDashboard_1.ID, entity.get(ScaffoldT24CustomerInformation.T24_ID));
        responseBody.set(ScaffoldCustomerDashboard_1.NAME, entity.get(ScaffoldT24CustomerInformation.T24_NAME));

        Map<String, String> homeAddress = new HashMap<>();
        Map<String, String> t24HomeAddress = (Map<String, String>) entity.get(ScaffoldT24CustomerInformation.T24_HOME_ADDRESS);
        homeAddress.put(ScaffoldCustomerDashboard_1.HOME_ADDRESS_LINE_1, t24HomeAddress.get(ScaffoldT24CustomerInformation.T24_HOME_ADDRESS_LINE_1));
        homeAddress.put(ScaffoldCustomerDashboard_1.HOME_ADDRESS_LINE_2, t24HomeAddress.get(ScaffoldT24CustomerInformation.T24_HOME_ADDRESS_LINE_2));
        homeAddress.put(ScaffoldCustomerDashboard_1.HOME_ADDRESS_POST_CODE, t24HomeAddress.get(ScaffoldT24CustomerInformation.T24_HOME_ADDRESS_POST_CODE));
        responseBody.set(ScaffoldCustomerDashboard_1.HOME_ADDRESS, homeAddress);

        Map<String, String> workAddress = new HashMap<>();
        Map<String, String> t24WorkAddress = (Map<String, String>) entity.get(ScaffoldT24CustomerInformation.T24_WORK_ADDRESS);
        workAddress.put(ScaffoldCustomerDashboard_1.WORK_ADDRESS_LINE_1, t24WorkAddress.get(ScaffoldT24CustomerInformation.T24_WORK_ADDRESS_LINE_1));
        workAddress.put(ScaffoldCustomerDashboard_1.WORK_ADDRESS_LINE_2, t24WorkAddress.get(ScaffoldT24CustomerInformation.T24_WORK_ADDRESS_LINE_2));
        workAddress.put(ScaffoldCustomerDashboard_1.WORK_ADDRESS_POST_CODE, t24WorkAddress.get(ScaffoldT24CustomerInformation.T24_WORK_ADDRESS_POST_CODE));
        responseBody.set(ScaffoldCustomerDashboard_1.WORK_ADDRESS, workAddress);

        List<Map<String, Object>> relatives = new ArrayList<>();
        List<Map<String, Object>> t24AccRelatives = entity.get(ScaffoldT24CustomerInformation.T24_RELATIVES, List.class);
        for(Map<String, Object> t24AccRelative : t24AccRelatives) {
            Map<String, Object> relative = new HashMap<>();
            relative.put(ScaffoldCustomerDashboard_1.RELATIVES_NAME, t24AccRelative.get(ScaffoldT24CustomerInformation.T24_RELATIVES_NAME));
            relative.put(ScaffoldCustomerDashboard_1.RELATIVES_RELATIONSHIP, t24AccRelative.get(ScaffoldT24CustomerInformation.T24_RELATIVES_RELATIONSHIP));
            relatives.add(relative);
        }
        responseBody.set(ScaffoldCustomerDashboard_1.RELATIVES, relatives);

        List<Map<String, Object>> accounts = new ArrayList<>();
        List<Entity> t24Accounts = entity.get(ScaffoldT24CustomerInformation.T24_ACCOUNTS, List.class);
        for(Entity t24Account : t24Accounts) {
            Map<String, Object> account = new HashMap<>();
            account.put(ScaffoldCustomerDashboard_1.ACCOUNTS_LABEL, t24Account.get(ScaffoldT24AccountInformation.T24_LABEL));
            account.put(ScaffoldCustomerDashboard_1.ACCOUNTS_NUMBER, t24Account.get(ScaffoldT24AccountInformation.T24_NUMBER));
            account.put(ScaffoldCustomerDashboard_1.ACCOUNTS_BALANCE, t24Account.get(ScaffoldT24AccountInformation.T24_BALANCE));

            List<Map<String, Object>> orders = new ArrayList<>();
            List<Entity> t24StdOrders = t24Account.get(ScaffoldT24AccountInformation.T24_STANDING_ORDERS, List.class);
            for(Entity t24StdOrder : t24StdOrders) {
                Map<String, Object> order = new HashMap<>();
                order.put(ScaffoldCustomerDashboard_1.ACCOUNTS_STANDING_ORDERS_TARGET, t24StdOrder.get(ScaffoldT24StandingOrder.T24_TARGET_ACCOUNT));
                order.put(ScaffoldCustomerDashboard_1.ACCOUNTS_STANDING_ORDERS_AMOUNT, t24StdOrder.get(ScaffoldT24StandingOrder.T24_AMOUNT));
                orders.add(order);
            }
            account.put(ScaffoldCustomerDashboard_1.ACCOUNTS_STANDING_ORDERS, orders);
            accounts.add(account);
        }
        responseBody.set(ScaffoldCustomerDashboard_1.ACCOUNTS, accounts);

        // construct response
        context.setResponseCode(Integer.toString(Response.Status.OK.getStatusCode()));
        context.setAttribute(intoDirective, responseBody);
    }
}
