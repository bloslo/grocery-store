package com.grocerystop.onlinegrocerystore.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDetails(List<ItemDto> items, List<ReceiptItem> receiptItems, BigDecimal total) {
}
