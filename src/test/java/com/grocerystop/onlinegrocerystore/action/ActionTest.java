package com.grocerystop.onlinegrocerystore.action;

import com.grocerystop.onlinegrocerystore.model.Item;
import com.grocerystop.onlinegrocerystore.model.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionTest {

    @Test
    void calculateDiscountAction() {
        Action dutchBeerPack = new Action("2.00 * nrOfBeerPacks");

        Item dutchBeer = new Item();
        dutchBeer.setName("Dutch beer");
        dutchBeer.setPrice(BigDecimal.valueOf(0.99));
        dutchBeer.setUnitOfMeasurement("bottle");

        OrderItem orderItem = new OrderItem();
        orderItem.setItem(dutchBeer);
        orderItem.setQuantity(12);
        orderItem.setUnitPrice(dutchBeer.getPrice());

        BigDecimal discount = dutchBeerPack.calculateDiscount(orderItem);
        assertEquals(BigDecimal.valueOf(4.00).setScale(2), discount.setScale(2));

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setItem(dutchBeer);
        orderItem1.setQuantity(20);
        orderItem1.setUnitPrice(dutchBeer.getPrice());

        discount = dutchBeerPack.calculateDiscount(orderItem1);
        assertEquals(BigDecimal.valueOf(6.00).setScale(2), discount.setScale(2));

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setItem(dutchBeer);
        orderItem2.setQuantity(1);
        orderItem2.setUnitPrice(dutchBeer.getPrice());

        discount = dutchBeerPack.calculateDiscount(orderItem2);
        assertEquals(BigDecimal.valueOf(0.00).setScale(2), discount);
    }
}
