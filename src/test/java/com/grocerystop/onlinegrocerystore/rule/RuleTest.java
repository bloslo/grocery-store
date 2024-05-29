package com.grocerystop.onlinegrocerystore.rule;

import com.grocerystop.onlinegrocerystore.expression.Expression;
import com.grocerystop.onlinegrocerystore.expression.ExpressionParser;
import com.grocerystop.onlinegrocerystore.model.Item;
import com.grocerystop.onlinegrocerystore.model.OrderItem;
import com.grocerystop.onlinegrocerystore.operations.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class RuleTest {

    private static final Operations operations = Operations.INSTANCE;

    @BeforeAll
    static void initAll() {
        operations.registerOperation(new And());
        operations.registerOperation(new Equals());
        operations.registerOperation(new MoreThan());
        operations.registerOperation(new LessThanOrEqual());
        operations.registerOperation(new MoreThanOrEqual());
        operations.registerOperation(new Between());
    }

    @Test
    void evaluateRule() {
        Expression condition = ExpressionParser.fromString("name = 'Vegetables' and quantity >= 0 and quantity <= 100");
        assertNotNull(condition);

        String action = "unitPrice * ( 5 / 100 )";

        Rule rule = new Rule.Builder()
                .withExpression(condition)
                .withAction(action)
                .build();

        Item vegetables = new Item();
        vegetables.setName("Vegetables");
        vegetables.setUnitOfMeasurement("100g");
        vegetables.setPrice(BigDecimal.valueOf(2.00));

        OrderItem vegetablesOrderItem = new OrderItem();
        vegetablesOrderItem.setItem(vegetables);
        vegetablesOrderItem.setQuantity(100);
        vegetablesOrderItem.setUnitPrice(BigDecimal.valueOf(2.00));

        assertTrue(rule.eval(vegetablesOrderItem));
        assertEquals(BigDecimal.valueOf(0.10).setScale(2), vegetablesOrderItem.getDiscount().setScale(2));
    }

    @Test
    void createRuleWithNullOrBlankAction() {
        Expression condition = ExpressionParser.fromString("name = 'Vegetables' and quantity > 100 and quantity <= 500");
        assertNotNull(condition);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Rule rule = new Rule.Builder()
                    .withExpression(condition)
                    .withAction(null)
                    .build();
        }, "Expected to throw IllegalArgumentException");
        assertEquals("Action cannot be null or blank", exception.getMessage());
    }

    @Test
    void createRuleWithoutAction() {
        Expression condition = ExpressionParser.fromString("name = 'Vegetables' and quantity > 100 and quantity <= 500");
        assertNotNull(condition);

        Rule rule = new Rule.Builder()
                .withExpression(condition)
                .build();

        Item vegetables = new Item();
        vegetables.setName("Vegetables");
        vegetables.setUnitOfMeasurement("100g");
        vegetables.setPrice(BigDecimal.valueOf(2.00));

        OrderItem vegetablesOrderItem = new OrderItem();
        vegetablesOrderItem.setItem(vegetables);
        vegetablesOrderItem.setQuantity(200);
        vegetablesOrderItem.setUnitPrice(BigDecimal.valueOf(2.00));

        assertTrue(rule.eval(vegetablesOrderItem));
        assertEquals(BigDecimal.valueOf(0), vegetablesOrderItem.getDiscount());
    }
}
