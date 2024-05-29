package com.grocerystop.onlinegrocerystore.repository;

import com.grocerystop.onlinegrocerystore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
