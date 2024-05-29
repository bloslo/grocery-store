package com.grocerystop.onlinegrocerystore.operations;

import com.grocerystop.onlinegrocerystore.expression.BaseType;
import com.grocerystop.onlinegrocerystore.expression.Expression;
import com.grocerystop.onlinegrocerystore.expression.Variable;
import com.grocerystop.onlinegrocerystore.model.OrderItem;

import java.util.Deque;

public class LessThanOrEqual extends Operation {

    public LessThanOrEqual() {
        super("<=");
    }

    @Override
    public Operation copy() {
        return new LessThanOrEqual();
    }

    @Override
    public int parse(String[] tokens, int position, Deque<Expression> stack) {
        if (position - 1 >= 0 && tokens.length >= position + 1) {
            String var = tokens[position - 1];

            this.leftOperand = new Variable(var);
            this.rightOperand = BaseType.getBaseType(tokens[position + 1]);
            stack.push(this);

            return position + 1;
        }
        throw new IllegalArgumentException("Cannot assign value to variable");
    }

    @Override
    public boolean interpret(OrderItem orderItem) {
        Variable var = (Variable) this.leftOperand;
        Object obj = var.getValue(orderItem);
        if (obj == null) {
            return false;
        }

        BaseType<?> type = (BaseType<?>) this.rightOperand;
        if (type.getType().equals(obj.getClass()) && (obj instanceof Comparable)) {
            if (((Comparable) obj).compareTo(type.getValue()) <= 0) {
                return true;
            }
        }
        return false;
    }
}
