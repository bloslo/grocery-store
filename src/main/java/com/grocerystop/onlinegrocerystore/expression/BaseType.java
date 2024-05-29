package com.grocerystop.onlinegrocerystore.expression;

import com.grocerystop.onlinegrocerystore.model.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BaseType<T> implements Expression {
    private T value;
    private Class<T> type;

    public BaseType(T value, Class<T> type) {
        this.value = value;
        this.type = type;
    }

    public T getValue() {
        return value;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public boolean interpret(OrderItem orderItem) {
        return true;
    }

    public static BaseType<?> getBaseType(String string) {
        if (string.isBlank()) {
            throw new IllegalArgumentException("The provided string must not be blank");
        }

        if ("true".equals(string) || "false".equals(string)) {
            return new BaseType<>(Boolean.valueOf(string), Boolean.class);
        } else if (string.startsWith("'")) {
            return new BaseType<>(string.substring(1, string.length() - 1), String.class);
        } else if (string.contains(".")) {
            return new BaseType<>(BigDecimal.valueOf(Double.valueOf(string)), BigDecimal.class);
        } else {
            return new BaseType<>(Integer.valueOf(string), Integer.class);
        }
    }
}
