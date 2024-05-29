package com.grocerystop.onlinegrocerystore.expression;

import com.grocerystop.onlinegrocerystore.model.OrderItem;

public interface Expression {
    boolean interpret(OrderItem orderItem);
}
