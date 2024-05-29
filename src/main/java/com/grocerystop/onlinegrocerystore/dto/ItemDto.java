package com.grocerystop.onlinegrocerystore.dto;

import java.math.BigDecimal;

public record ItemDto(String name, BigDecimal price, String unit) {
}
