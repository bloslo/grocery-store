package com.grocerystop.onlinegrocerystore.expression;

import com.grocerystop.onlinegrocerystore.model.Item;
import com.grocerystop.onlinegrocerystore.model.OrderItem;
import com.grocerystop.onlinegrocerystore.operations.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ExpressionTest {
    private static final Operations operations = Operations.INSTANCE;

    @BeforeAll
    static void initAll() {
        operations.registerOperation(new And());
        operations.registerOperation(new Equals());
        operations.registerOperation(new MoreThan());
        operations.registerOperation(new LessThanOrEqual());
        operations.registerOperation(new MoreThanOrEqual());
        operations.registerOperation(new Between());
        operations.registerOperation(new Contains());
    }

    @Test
    void parseExpression() {
        Expression expression = ExpressionParser.fromString("name = 'Bread' and quantity = 2");
        assertNotNull(expression);

        Item bread = new Item();
        bread.setName("Bread");
        bread.setPrice(BigDecimal.valueOf(2.00));
        bread.setUnitOfMeasurement("none");

        Item vegetables = new Item();
        vegetables.setUnitOfMeasurement("100g");
        vegetables.setPrice(BigDecimal.valueOf(3.00));
        vegetables.setName("Vegetables");

        OrderItem breadOrderItem = new OrderItem();
        breadOrderItem.setItem(bread);
        breadOrderItem.setProduced(LocalDate.of(2024, 5, 18));
        breadOrderItem.setQuantity(2);
        breadOrderItem.setUnitPrice(BigDecimal.valueOf(2.00));

        assertTrue(expression.interpret(breadOrderItem));

        OrderItem vegetablesOrderItem = new OrderItem();
        vegetablesOrderItem.setItem(vegetables);
        vegetablesOrderItem.setQuantity(300);
        vegetablesOrderItem.setUnitPrice(BigDecimal.valueOf(3.00));

        assertFalse(expression.interpret(vegetablesOrderItem));
    }

    @Test
    void parseMoreThanExpression() {
        Expression expression = ExpressionParser.fromString("name = 'Vegetables' and quantity > 500");
        assertNotNull(expression);

        Item vegetables = new Item();
        vegetables.setUnitOfMeasurement("100g");
        vegetables.setPrice(BigDecimal.valueOf(3.00));
        vegetables.setName("Vegetables");

        OrderItem vegetablesOrderItem = new OrderItem();
        vegetablesOrderItem.setItem(vegetables);
        vegetablesOrderItem.setQuantity(600);
        vegetablesOrderItem.setUnitPrice(BigDecimal.valueOf(3.00));

        assertTrue(expression.interpret(vegetablesOrderItem));

        OrderItem vegetablesOrderItem2 = new OrderItem();
        vegetablesOrderItem2.setItem(vegetables);
        vegetablesOrderItem2.setQuantity(400);
        vegetablesOrderItem2.setUnitPrice(BigDecimal.valueOf(3.00));

        assertFalse(expression.interpret(vegetablesOrderItem2));
    }

    @Test
    void parseLessThanOrEqualExpression() {
        Expression expression = ExpressionParser
                .fromString("name = 'Vegetables' and quantity <= 500");
        assertNotNull(expression);

        Item vegetables = new Item();
        vegetables.setUnitOfMeasurement("100g");
        vegetables.setPrice(BigDecimal.valueOf(3.00));
        vegetables.setName("Vegetables");

        OrderItem vegetablesOrderItem = new OrderItem();
        vegetablesOrderItem.setItem(vegetables);
        vegetablesOrderItem.setQuantity(500);
        vegetablesOrderItem.setUnitPrice(BigDecimal.valueOf(3.00));

        assertTrue(expression.interpret(vegetablesOrderItem));

        OrderItem vegetablesOrderItem2 = new OrderItem();
        vegetablesOrderItem2.setItem(vegetables);
        vegetablesOrderItem2.setQuantity(520);
        vegetablesOrderItem2.setUnitPrice(BigDecimal.valueOf(3.00));

        assertFalse(expression.interpret(vegetablesOrderItem2));
    }

    @Test
    void parseMoreThanOrEqualExpression() {
        Expression expression = ExpressionParser
                .fromString("name = 'Vegetables' and quantity >= 100 and quantity <= 500");
        assertNotNull(expression);

        Item vegetables = new Item();
        vegetables.setUnitOfMeasurement("100g");
        vegetables.setPrice(BigDecimal.valueOf(3.00));
        vegetables.setName("Vegetables");

        OrderItem vegetablesOrderItem = new OrderItem();
        vegetablesOrderItem.setItem(vegetables);
        vegetablesOrderItem.setQuantity(350);
        vegetablesOrderItem.setUnitPrice(BigDecimal.valueOf(3.00));

        assertTrue(expression.interpret(vegetablesOrderItem));

        OrderItem vegetablesOrderItem2 = new OrderItem();
        vegetablesOrderItem2.setItem(vegetables);
        vegetablesOrderItem2.setQuantity(50);
        vegetablesOrderItem2.setUnitPrice(BigDecimal.valueOf(3.00));

        assertFalse(expression.interpret(vegetablesOrderItem2));
    }

    @Test
    void parseBetweenExpression() {
        Expression expression = ExpressionParser
                .fromString("produced between '3days'");
        assertNotNull(expression);

        Item bread = new Item();
        bread.setName("Bread");
        bread.setPrice(BigDecimal.valueOf(2.00));
        bread.setUnitOfMeasurement("none");

        OrderItem breadOrderItem = new OrderItem();
        breadOrderItem.setItem(bread);
        breadOrderItem.setProduced(LocalDate.of(2024, 5, 18));
        breadOrderItem.setQuantity(2);
        breadOrderItem.setUnitPrice(BigDecimal.valueOf(2.00));

        assertFalse(expression.interpret(breadOrderItem));

        OrderItem breadOrderItem1 = new OrderItem();
        breadOrderItem1.setItem(bread);
        breadOrderItem1.setProduced(LocalDate.now().minusDays(3));
        breadOrderItem1.setQuantity(2);
        breadOrderItem1.setUnitPrice(BigDecimal.valueOf(2.00));

        assertTrue(expression.interpret(breadOrderItem1));
    }

    @Test
    void parseContainsExpression() {
        Expression expression = ExpressionParser
                .fromString("description contains 'Dutch beer' and quantity >= 6");
        assertNotNull(expression);

        Item dutchBeer = new Item();
        dutchBeer.setName("Heineken");
        dutchBeer.setDescription("Dutch beer");
        dutchBeer.setPrice(BigDecimal.valueOf(2.00));
        dutchBeer.setUnitOfMeasurement("bottle");

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(dutchBeer);
        orderItem.setUnitPrice(dutchBeer.getPrice());
        orderItem.setQuantity(6);

        assertTrue(expression.interpret(orderItem));
    }
}
