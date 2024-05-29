package com.grocerystop.onlinegrocerystore.expression;

import com.grocerystop.onlinegrocerystore.model.OrderItem;

public class Variable implements Expression {
    private String name;

    public Variable(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean interpret(OrderItem orderItem) {
        return true;
    }

    public Object getValue(OrderItem orderItem) {
        return switch (name) {
            case "name" -> orderItem.getItem().getName();
            case "quantity" -> orderItem.getQuantity();
            case "produced" -> orderItem.getProduced();
            case "description" -> orderItem.getItem().getDescription();
            default -> null;
        };
    }
}
