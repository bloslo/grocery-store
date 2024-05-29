package com.grocerystop.onlinegrocerystore.repository;

import com.grocerystop.onlinegrocerystore.model.OrderItem;
import com.grocerystop.onlinegrocerystore.model.OrderItemId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
}
