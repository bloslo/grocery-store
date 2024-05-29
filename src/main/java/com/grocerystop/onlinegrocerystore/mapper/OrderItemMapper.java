package com.grocerystop.onlinegrocerystore.mapper;

import com.grocerystop.onlinegrocerystore.dto.OrderItemDto;
import com.grocerystop.onlinegrocerystore.model.OrderItem;

public class OrderItemMapper {
    public static OrderItem fromDto(OrderItemDto orderItemDto) {
        OrderItem orderItem = new OrderItem();

        orderItem.setQuantity(orderItemDto.quantity());
        return orderItem;
    }
}
