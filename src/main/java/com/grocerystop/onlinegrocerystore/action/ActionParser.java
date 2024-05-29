package com.grocerystop.onlinegrocerystore.action;

import java.math.BigDecimal;
import java.util.*;

public class ActionParser {

    public static BigDecimal fromString(String action) {
        String[] str = action.split("\\s+");
        Queue<String> q = new LinkedList<>();
        q.addAll(Arrays.asList(str));
        Deque<String> ops = new ArrayDeque<>();
        Deque<BigDecimal> vals = new ArrayDeque<>();

        while (!q.isEmpty()) {
            String token = q.poll();

            switch (token) {
                case "(" -> {}
                case "+","-","/","*" -> ops.push(token);
                case ")" -> vals.push(evaluateOp(ops, vals));
                default -> vals.push(new BigDecimal(token));
            }
        }

        if (!ops.isEmpty()) {
            return evaluateOp(ops, vals);
        }

        return vals.pop();
    }

    private static BigDecimal evaluateOp(Deque<String> ops, Deque<BigDecimal> vals) {
        BigDecimal val = vals.pop();

        if (!ops.isEmpty()) {
            String op = ops.pop();

            switch (op) {
                case "+" -> val = vals.pop().add(val);
                case "-" -> val = vals.pop().subtract(val);
                case "*" -> val = vals.pop().multiply(val);
                case "/" -> val = vals.pop().divide(val);
            }
        }

        return val;
    }
}
