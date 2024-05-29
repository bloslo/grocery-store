package com.grocerystop.onlinegrocerystore.dto;

import java.math.BigDecimal;

public record ReceiptItem(String name, int quantity, BigDecimal price) {
}
