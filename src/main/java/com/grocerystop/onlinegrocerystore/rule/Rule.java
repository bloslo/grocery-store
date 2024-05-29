package com.grocerystop.onlinegrocerystore.rule;

import com.grocerystop.onlinegrocerystore.action.Action;
import com.grocerystop.onlinegrocerystore.expression.Expression;
import com.grocerystop.onlinegrocerystore.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Rule {

    private static final Logger logger = LoggerFactory.getLogger(Rule.class);

    private List<Expression> expressions;
    private Action action;

    public static class Builder {
        private List<Expression> expressions = new ArrayList<>();
        private Action action = new Action("0");

        public Builder withExpression(Expression expression) {
            expressions.add(expression);
            return this;
        }

        public Builder withAction(String action) {
            if (action == null || action.isBlank()) {
                throw new IllegalArgumentException("Action cannot be null or blank");
            }

            this.action = new Action(action);
            return this;
        }

        public Rule build() {
            return new Rule(expressions, action);
        }
    }

    private Rule(List<Expression> expressions, Action action) {
        this.expressions = expressions;
        this.action = action;
    }

    public boolean eval(OrderItem orderItem) {
        boolean eval = false;
        for (Expression expression : expressions) {
            eval = expression.interpret(orderItem);
            if (eval && action != null) {
                BigDecimal discount = action.calculateDiscount(orderItem);
                orderItem.setDiscount(discount);
            }
        }

        return eval;
    }
}
