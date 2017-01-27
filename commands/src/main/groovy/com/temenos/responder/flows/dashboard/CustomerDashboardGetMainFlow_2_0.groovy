package com.temenos.responder.flows.dashboard;

import com.temenos.responder.commands.Command;
import com.temenos.responder.commands.dashboard.T24AccountInformation;
import com.temenos.responder.commands.dashboard.T24CustomerInformation;
import com.temenos.responder.commands.dashboard.T24StandingOrder;
import com.temenos.responder.commands.transformers.dashboard.CustomerDashboardTransformer_2;
import com.temenos.responder.context.CommandContext;
import com.temenos.responder.context.DefaultCommandContext;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.AbstractFlow;
import com.temenos.responder.scaffold.dashboard.ScaffoldT24AccountInformation;
import com.temenos.responder.scaffold.dashboard.ScaffoldT24CustomerInformation;

import javax.ws.rs.core.Response;

/**
 * This flow retrieves customer information from T24, comprising its accounts and their
 * respective standing orders.
 *
 * Created by aburgos on 18/01/2017.
 */
class CustomerDashboardGetMainFlow_2_0 extends AbstractFlow {

    @Override
    public void doExecute(ExecutionContext executionContext) {
        List<String> from = new ArrayList<>(Collections.singletonList("customerId"));
        String into = "finalResult";

        // get external customer information
        Command customerInfoCmd = new T24CustomerInformation();

        // setup command context
        CommandContext customerCtx = new DefaultCommandContext();
        customerCtx.setAttribute(from.get(0), executionContext.getAttribute(from.get(0)));

        // execute the command
        customerInfoCmd.execute(customerCtx);

        Entity t24Customer = (Entity) customerCtx.getAttribute(into);

        // check response from T24 customer information
        if (!customerCtx.getResponseCode().equals(Integer.toString(Response.Status.OK.getStatusCode())) || t24Customer.getAccessors().isEmpty()) {
            executionContext.setResponseCode(customerCtx.getResponseCode());
            executionContext.setAttribute(into, new Entity());
            return;
        }

        List<Entity> accounts = new ArrayList<>();
        List<String> customerAccountIds = (List<String>) t24Customer.get(ScaffoldT24CustomerInformation.T24_ACCOUNTS);
        for(String customerAccountId : customerAccountIds) {
            // get external customer information
            Command accInfoCmd = new T24AccountInformation();

            // setup command context
            CommandContext accCtx = new DefaultCommandContext();
            accCtx.setAttribute("accountId", customerAccountId);

            // execute the command
            accInfoCmd.execute(accCtx);

            Entity t24Account = (Entity) accCtx.getAttribute("finalResult");

            if(t24Account.getAccessors().isEmpty()) continue;

            // check response from T24 account information
            if (!accCtx.getResponseCode().equals(Integer.toString(Response.Status.OK.getStatusCode()))) {
                executionContext.setResponseCode(accCtx.getResponseCode());
                executionContext.setAttribute(into, new Entity());
                return;
            }

            List<Entity> orders = new ArrayList<>();
            List<String> accountStandingOrderIds = (List<String>) t24Account.get(ScaffoldT24AccountInformation.T24_STANDING_ORDERS);
            for(String accountStandingOrderId : accountStandingOrderIds) {
                // get external customer information
                Command stoCmd = new T24StandingOrder();

                // setup command context
                CommandContext stoCtx = new DefaultCommandContext();
                stoCtx.setAttribute("standingOrderId", accountStandingOrderId);

                // execute the command
                stoCmd.execute(stoCtx);

                Entity t24StandingOrder = (Entity) stoCtx.getAttribute("finalResult");

                if(t24StandingOrder.getAccessors().isEmpty()) continue;

                // check response from T24 account information
                if (!stoCtx.getResponseCode().equals(Integer.toString(Response.Status.OK.getStatusCode()))) {
                    executionContext.setResponseCode(stoCtx.getResponseCode());
                    executionContext.setAttribute(into, new Entity());
                    return;
                }

                orders.add(t24StandingOrder);
            }
            t24Account.set(ScaffoldT24AccountInformation.T24_STANDING_ORDERS, orders);
            accounts.add(t24Account);
        }
        t24Customer.set(ScaffoldT24CustomerInformation.T24_ACCOUNTS, accounts);

        // transform external customer model into internal customer model
        Command transformer = new CustomerDashboardTransformer_2();
        CommandContext trnsCmd = new DefaultCommandContext(new ArrayList<>(Collections.singletonList("ExtnCustomer")), "finalResult");
        trnsCmd.setAttribute("ExtnCustomer", t24Customer);
        transformer.execute(trnsCmd);

        Entity responseBody = (Entity) trnsCmd.getAttribute("finalResult");
        executionContext.setResponseCode(Integer.toString(Response.Status.OK.getStatusCode()));
        executionContext.setAttribute(into, responseBody);
    }
}