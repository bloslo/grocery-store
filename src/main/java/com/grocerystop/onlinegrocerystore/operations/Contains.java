package com.grocerystop.onlinegrocerystore.operations;

import com.grocerystop.onlinegrocerystore.expression.BaseType;
import com.grocerystop.onlinegrocerystore.expression.Expression;
import com.grocerystop.onlinegrocerystore.expression.ExpressionUtils;
import com.grocerystop.onlinegrocerystore.expression.Variable;
import com.grocerystop.onlinegrocerystore.model.OrderItem;

import java.util.Arrays;
import java.util.Deque;
import java.util.stream.Collectors;

public class Contains extends Operation {

    public Contains() {
        super("contains");
    }

    @Override
    public Operation copy() {
        return new Contains();
    }

    @Override
    public int parse(String[] tokens, int position, Deque<Expression> stack) {
        if (position - 1 >= 0 && tokens.length >= position + 1) {
            String var = tokens[position - 1];

            this.leftOperand = new Variable(var);
            if (tokens[position + 1].startsWith("'") && !tokens[position + 1].endsWith("'")) {
                int strEndPos = ExpressionUtils.findStringEnding(tokens, position + 1);
                String strValue = Arrays.stream(tokens, position + 1, strEndPos + 1)
                        .collect(Collectors.joining(" "));
                this.rightOperand = BaseType.getBaseType(strValue);
            } else {
                this.rightOperand = BaseType.getBaseType(tokens[position + 1]);
            }
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
        if (type.getType().equals(obj.getClass()) && obj instanceof String) {
            if (type.getValue().toString().contains(obj.toString())) {
                return true;
            }
        }
        return false;
    }
}
