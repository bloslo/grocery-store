package com.grocerystop.onlinegrocerystore.operations;

import com.grocerystop.onlinegrocerystore.expression.BaseType;
import com.grocerystop.onlinegrocerystore.expression.Expression;
import com.grocerystop.onlinegrocerystore.expression.Variable;
import com.grocerystop.onlinegrocerystore.model.OrderItem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Between extends Operation {

    public Between() {
        super("between");
    }

    @Override
    public Operation copy() {
        return new Between();
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
        if (obj instanceof LocalDate && ((LocalDate) obj).isBefore(LocalDate.now())
                && type.getType().equals(String.class)) {
            Pattern regex = Pattern.compile("\\d+");
            Matcher matcher = regex.matcher((String) type.getValue());
            if (matcher.find()) {
                long diff = ChronoUnit.DAYS.between((LocalDate) obj, LocalDate.now());
                long days = Long.valueOf(matcher.group());
                System.out.println(diff);
                if (diff == days) {
                    return true;
                }
            }
        }
        return false;
    }
}
