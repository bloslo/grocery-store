package com.grocerystop.onlinegrocerystore.service;

import com.grocerystop.onlinegrocerystore.dto.*;
import com.grocerystop.onlinegrocerystore.exception.ItemNotFoundException;
import com.grocerystop.onlinegrocerystore.mapper.ItemMapper;
import com.grocerystop.onlinegrocerystore.model.Item;
import com.grocerystop.onlinegrocerystore.model.Order;
import com.grocerystop.onlinegrocerystore.model.OrderItem;
import com.grocerystop.onlinegrocerystore.model.OrderItemId;
import com.grocerystop.onlinegrocerystore.repository.ItemRepository;
import com.grocerystop.onlinegrocerystore.repository.OrderItemRepository;
import com.grocerystop.onlinegrocerystore.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroceryStoreService {

    private static final Logger logger = LoggerFactory.getLogger(GroceryStoreService.class);

    private OrderRepository orderRepository;
    private ItemRepository itemRepository;
    private OrderItemRepository orderItemRepository;
    private RuleService ruleService;

    public GroceryStoreService(OrderRepository orderRepository, ItemRepository itemRepository,
                               OrderItemRepository orderItemRepository, RuleService ruleService) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
        this.orderItemRepository = orderItemRepository;
        this.ruleService = ruleService;
    }

    public Page<RuleDto> getAllRules(int page, int size) {
        return this.ruleService.getAllRules(page,size);
    }

    public List<ItemDto> getCurrentPricesPerItem() {
        List<Item> items = itemRepository.findAll();
        return items.stream().map(ItemMapper::toDto).collect(Collectors.toUnmodifiableList());
    }

    public OrderDetails createOrder(OrderRequest order) {
        Order newOrder = new Order();
        newOrder.setDate(OffsetDateTime.now());
        logger.info("Saving initial order...");
        newOrder = orderRepository.save(newOrder);

        List<OrderItem> orderItems = createOrderItemList(order.orderItems(), newOrder);
        orderItems = orderItemRepository.saveAll(orderItems);

        orderItems = ruleService.calculateDiscounts(orderItems);
        orderItems = orderItemRepository.saveAll(orderItems);

        OrderDetails orderDetails = calculateOrderReceipt(orderItems);
        newOrder.setTotal(orderDetails.total().setScale(2));
        orderRepository.save(newOrder);

        return orderDetails;
    }

    private List<OrderItem> createOrderItemList(List<OrderItemDto> orderItemDtos, Order newOrder) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDto orderItemDto : orderItemDtos) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(new OrderItemId());
            orderItem.setOrder(newOrder);
            orderItem.setQuantity(orderItemDto.quantity());

            if (orderItemDto.produced() != null) {
                orderItem.setProduced(orderItemDto.produced());
            }

            Item item = itemRepository.findByName(orderItemDto.name());
            if (item == null) {
                orderRepository.delete(newOrder);
                throw new ItemNotFoundException("Item not found");
            }
            orderItem.setItem(item);
            orderItem.setUnitPrice(item.getPrice());
            orderItem.setDiscount(BigDecimal.ZERO);

            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private OrderDetails calculateOrderReceipt(List<OrderItem> orderItemsWithDiscount) {
        BigDecimal total = BigDecimal.valueOf(0L, 2);
        List<ReceiptItem> receiptItems = new ArrayList<>();
        List<ItemDto> items = new ArrayList<>();

        for (OrderItem orderItem : orderItemsWithDiscount) {
            BigDecimal orderItemPrice = BigDecimal.valueOf(0L, 2);
            String daysOld = "";

            if (orderItem.getItem().getName().toLowerCase().contains("beer")) {
                if (orderItem.getDiscount() != null && orderItem.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
                    orderItemPrice = orderItemPrice.add(orderItem.getDiscount());
                } else {
                    orderItemPrice = orderItemPrice.add(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
                }
            } else if (orderItem.getItem().getName().toLowerCase().equals("bread")) {
                if (orderItem.getDiscount() != null) {
                    orderItemPrice = orderItemPrice.add(
                            orderItem.getUnitPrice()
                                    .multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                                    .subtract(orderItem.getDiscount()));
                } else {
                    orderItemPrice = orderItemPrice.add(orderItem.getUnitPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
                }

                daysOld = "(" + ChronoUnit.DAYS.between(orderItem.getProduced(), LocalDate.now()) + " days old)";
            } else {
                BigDecimal priceWithoutDiscount = orderItem.getUnitPrice()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                        .divide(BigDecimal.valueOf(100));
                BigDecimal discount = priceWithoutDiscount.multiply(orderItem.getDiscount());
                orderItemPrice = orderItemPrice.add(priceWithoutDiscount.subtract(discount));
            }

            items.add(new ItemDto(orderItem.getItem().getName(), orderItem.getItem().getPrice().setScale(2),
                    orderItem.getItem().getUnitOfMeasurement()));
            receiptItems.add(new ReceiptItem(orderItem.getItem().getName() + " " + daysOld,
                    orderItem.getQuantity(), orderItemPrice.setScale(2)));
            total = total.add(orderItemPrice);
        }

        return new OrderDetails(items, receiptItems, total.setScale(2));
    }
}
