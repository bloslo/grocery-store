package com.grocerystop.onlinegrocerystore.operations;

import com.grocerystop.onlinegrocerystore.model.OrderItem;
import com.grocerystop.onlinegrocerystore.expression.Expression;

import java.util.Deque;

public class And extends Operation {
    public And() {
        super("and");
    }

    @Override
    public Operation copy() {
        return new And();
    }

    @Override
    public int parse(String[] tokens, int position, Deque<Expression> stack) {
        Expression left = stack.pop();
        int i = findNextExpression(tokens, position + 1, stack);
        Expression right = stack.pop();

        this.leftOperand = left;
        this.rightOperand = right;
        stack.push(this);

        return i;
    }

    @Override
    public boolean interpret(OrderItem orderItem) {
        return leftOperand.interpret(orderItem) && rightOperand.interpret(orderItem);
    }
}
