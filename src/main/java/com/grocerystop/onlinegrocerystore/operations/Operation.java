package com.grocerystop.onlinegrocerystore.operations;

import com.grocerystop.onlinegrocerystore.expression.Expression;

import java.util.Deque;

public abstract class Operation implements Expression {
    private String symbol;
    protected Expression leftOperand;
    protected Expression rightOperand;

    public Operation(String symbol) {
        this.symbol = symbol;
    }

    public abstract Operation copy();

    public String getSymbol() {
        return symbol;
    }

    public abstract int parse(String[] tokens, int position, Deque<Expression> stack);

    Integer findNextExpression(String[] tokens, int position, Deque<Expression> stack) {
        Operations operations = Operations.INSTANCE;

        for (int i = position; i < tokens.length; i++) {
            Operation op = operations.getOperation(tokens[i]);
            if (op != null) {
                op = op.copy();
                i = op.parse(tokens, i, stack);
                return i;
            }
        }
        return null;
    }
}
