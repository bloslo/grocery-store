package com.grocerystop.onlinegrocerystore.mapper;

import com.grocerystop.onlinegrocerystore.dto.ItemDto;
import com.grocerystop.onlinegrocerystore.model.Item;

public class ItemMapper {
    public static ItemDto toDto(Item item) {
        return new ItemDto(item.getName(), item.getPrice(), item.getUnitOfMeasurement());
    }
}
