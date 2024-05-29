package com.grocerystop.onlinegrocerystore.dto;

import java.util.List;

public record OrderRequest(List<OrderItemDto> orderItems) {
}
