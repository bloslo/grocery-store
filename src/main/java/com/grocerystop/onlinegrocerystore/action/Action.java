package com.grocerystop.onlinegrocerystore.action;

import com.grocerystop.onlinegrocerystore.model.OrderItem;

import java.math.BigDecimal;

public class Action {

    private String action;

    public Action(String action) {
        this.action = action;
    }

    public BigDecimal calculateDiscount(OrderItem orderItem) {
        String finalAction = replaceVariablesWithValues(orderItem);
        return ActionParser.fromString(finalAction);
    }

    private String replaceVariablesWithValues(OrderItem orderItem) {
        String actionWithValues;
        actionWithValues = action.replace("nrOfBeerPacks", String.valueOf(orderItem.getQuantity() / 6));
        actionWithValues = actionWithValues.replace("unitPrice", String.valueOf(orderItem.getUnitPrice()));
        return actionWithValues;
    }
}
