package com.temenos.responder.flows.dashboard;

import com.temenos.responder.commands.Command;
import com.temenos.responder.commands.dashboard.T24AccountInformation;
import com.temenos.responder.commands.dashboard.T24StandingOrder;
import com.temenos.responder.commands.transformers.dashboard.CustomerDashboardTransformer_1;
import com.temenos.responder.context.CommandContext;
import com.temenos.responder.context.DefaultCommandContext;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.commands.dashboard.T24CustomerInformation;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.AbstractFlow;
import com.temenos.responder.scaffold.dashboard.ScaffoldT24AccountInformation;
import com.temenos.responder.scaffold.dashboard.ScaffoldT24CustomerInformation;

import javax.ws.rs.core.Response;
import java.util.*;

/**
 * This flow retrieves customer information from T24, comprising its accounts and their
 * respective standing orders.
 *
 * Created by aburgos on 17/01/2017.
 */
public class CustomerDashboardGetMainFlow_1_0 extends AbstractFlow {

    @Override
    public void doExecute(ExecutionContext executionContext) {
        // setup command context
        CommandContext customerCtx = new DefaultCommandContext();
        customerCtx.setAttribute("customerId", executionContext.getAttribute("customerId"));

        // get T24 customer command and execute with customer context
        executionContext.getCommand(T24CustomerInformation.class).execute(customerCtx);
        // get command resulting entity
        Entity t24Customer = (Entity) customerCtx.getAttribute("finalResult");

        // check response from T24 customer information
        if (!customerCtx.getResponseCode().equals(Integer.toString(Response.Status.OK.getStatusCode())) || t24Customer.getAccessors().isEmpty()) {
            executionContext.setResponseCode(customerCtx.getResponseCode());
            executionContext.setAttribute("finalResult", new Entity());
            return;
        }

        List<Entity> accounts = new ArrayList<>();
        List<String> customerAccountIds = (List<String>) t24Customer.get(ScaffoldT24CustomerInformation.T24_ACCOUNTS);
        for(String customerAccountId : customerAccountIds) {
            // setup command context
            CommandContext accCtx = new DefaultCommandContext();
            accCtx.setAttribute("accountId", customerAccountId);

            // get T24 account command and execute with account context
            executionContext.getCommand(T24AccountInformation.class).execute(accCtx);
            // get command resulting entity
            Entity t24Account = (Entity) accCtx.getAttribute("finalResult");

            if(t24Account.getAccessors().isEmpty()) continue;

            // check response from T24 account information
            if (!accCtx.getResponseCode().equals(Integer.toString(Response.Status.OK.getStatusCode()))) {
                executionContext.setResponseCode(accCtx.getResponseCode());
                executionContext.setAttribute("finalResult", new Entity());
                return;
            }

            List<Entity> orders = new ArrayList<>();
            List<String> accountStandingOrderIds = (List<String>) t24Account.get(ScaffoldT24AccountInformation.T24_STANDING_ORDERS);
            for(String accountStandingOrderId : accountStandingOrderIds) {
                // setup command context
                CommandContext stoCtx = new DefaultCommandContext();
                stoCtx.setAttribute("standingOrderId", accountStandingOrderId);

                // get T24 standing order command and execute with standing order context
                executionContext.getCommand(T24StandingOrder.class).execute(stoCtx);
                // get command resulting entity
                Entity t24StandingOrder = (Entity) stoCtx.getAttribute("finalResult");

                if(t24StandingOrder.getAccessors().isEmpty()) continue;

                // check response from T24 account information
                if (!stoCtx.getResponseCode().equals(Integer.toString(Response.Status.OK.getStatusCode()))) {
                    executionContext.setResponseCode(stoCtx.getResponseCode());
                    executionContext.setAttribute("finalResult", new Entity());
                    return;
                }

                orders.add(t24StandingOrder);
            }
            t24Account.set(ScaffoldT24AccountInformation.T24_STANDING_ORDERS, orders);
            accounts.add(t24Account);
        }
        t24Customer.set(ScaffoldT24CustomerInformation.T24_ACCOUNTS, accounts);

        // transform external customer model into internal customer model
        Command transformer = new CustomerDashboardTransformer_1();
        CommandContext trnsCmd = new DefaultCommandContext(new ArrayList<>(Collections.singletonList("ExtnCustomer")), "finalResult");
        trnsCmd.setAttribute("ExtnCustomer", t24Customer);
        transformer.execute(trnsCmd);

        Entity responseBody = (Entity) trnsCmd.getAttribute("finalResult");
        executionContext.setResponseCode(Integer.toString(Response.Status.OK.getStatusCode()));
        executionContext.setAttribute("finalResult", responseBody);
    }
}