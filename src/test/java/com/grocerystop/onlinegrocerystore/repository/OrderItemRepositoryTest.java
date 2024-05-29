package com.grocerystop.onlinegrocerystore.repository;

import com.grocerystop.onlinegrocerystore.model.Item;
import com.grocerystop.onlinegrocerystore.model.Order;
import com.grocerystop.onlinegrocerystore.model.OrderItem;
import com.grocerystop.onlinegrocerystore.model.OrderItemId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class OrderItemRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    void addOrderItem() {
        Order newOrder = new Order();
        newOrder.setDate(OffsetDateTime.now());
        newOrder = orderRepository.save(newOrder);

        assertNotNull(newOrder.getId());

        Item item = itemRepository.findByName("Bread");

        OrderItem orderItem = new OrderItem();
        OrderItemId orderItemId = new OrderItemId();

        orderItem.setId(orderItemId);
        orderItem.setItem(item);
        orderItem.setOrder(newOrder);
        orderItem.setQuantity(1);
        orderItem.setProduced(LocalDate.now());
        orderItem.setUnitPrice(BigDecimal.valueOf(1.00));
        orderItem = orderItemRepository.save(orderItem);

        assertThat(orderItem.getOrder()).isNotNull();

        List<OrderItem> orderItems = orderItemRepository.findAll();
        assertThat(orderItems).hasSize(1);
    }
}
