package com.grocerystop.onlinegrocerystore.controller;

import com.grocerystop.onlinegrocerystore.dto.*;
import com.grocerystop.onlinegrocerystore.exception.ItemNotFoundException;
import com.grocerystop.onlinegrocerystore.service.GroceryStoreService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GroceryStoreController {

    private GroceryStoreService groceryStoreService;

    public GroceryStoreController(GroceryStoreService groceryStoreService) {
        this.groceryStoreService = groceryStoreService;
    }

    @GetMapping("/api/v1/rules")
    public Page<RuleDto> getAllRules(@RequestParam int page, @RequestParam int size) {
        return this.groceryStoreService.getAllRules(page, size);
    }

    @GetMapping("/api/v1/prices")
    public List<ItemDto> getCurrentPrices() {
        return this.groceryStoreService.getCurrentPricesPerItem();
    }

    @PostMapping("/api/v1/order")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDetails getOrderDetails(@RequestBody OrderRequest order) {
        return this.groceryStoreService.createOrder(order);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemNotFoundException(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
