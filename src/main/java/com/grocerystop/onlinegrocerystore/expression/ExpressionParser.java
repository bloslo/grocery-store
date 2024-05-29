package com.grocerystop.onlinegrocerystore.expression;

import com.grocerystop.onlinegrocerystore.operations.Operation;
import com.grocerystop.onlinegrocerystore.operations.Operations;

import java.util.ArrayDeque;
import java.util.Deque;

public class ExpressionParser {
    private static final Operations operations = Operations.INSTANCE;

    public static Expression fromString(String expression) {
        Deque<Expression> stack = new ArrayDeque<>();

        String[] tokens = expression.split("\\s");
        for (int i = 0; i < tokens.length - 1; i++) {
            Operation op = operations.getOperation(tokens[i]);
            if (op != null) {
                op = op.copy();
                i = op.parse(tokens, i, stack);
            }
        }

        return stack.pop();
    }
}
