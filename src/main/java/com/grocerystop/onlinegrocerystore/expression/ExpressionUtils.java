package com.grocerystop.onlinegrocerystore.expression;

public class ExpressionUtils {

    public static int findStringEnding(String[] tokens, int position) {
        for (int i = position; i < tokens.length; i++) {
            if (tokens[i].endsWith("'")) {
                return i;
            }
        }
        return 0;
    }
}
