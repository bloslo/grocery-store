package com.grocerystop.onlinegrocerystore.dto;

import java.time.LocalDate;

public record OrderItemDto(String name, Integer quantity, LocalDate produced) {
}
