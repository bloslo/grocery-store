package com.grocerystop.onlinegrocerystore.service;

import com.grocerystop.onlinegrocerystore.dto.RuleDto;
import com.grocerystop.onlinegrocerystore.expression.ExpressionParser;
import com.grocerystop.onlinegrocerystore.mapper.RuleMapper;
import com.grocerystop.onlinegrocerystore.model.OrderItem;
import com.grocerystop.onlinegrocerystore.model.RuleAction;
import com.grocerystop.onlinegrocerystore.operations.*;
import com.grocerystop.onlinegrocerystore.repository.RuleActionRepository;
import com.grocerystop.onlinegrocerystore.rule.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleService {

    private Logger logger = LoggerFactory.getLogger(RuleService.class);

    private RuleActionRepository ruleActionRepository;

    private final Operations operations = Operations.INSTANCE;

    public RuleService(RuleActionRepository ruleActionRepository) {
        this.ruleActionRepository = ruleActionRepository;

        this.operations.registerOperation(new And());
        this.operations.registerOperation(new Equals());
        this.operations.registerOperation(new MoreThan());
        this.operations.registerOperation(new LessThanOrEqual());
        this.operations.registerOperation(new MoreThanOrEqual());
        this.operations.registerOperation(new Between());
    }

    public Page<RuleDto> getAllRules(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<RuleAction> ruleActions = ruleActionRepository.findAll(pageRequest);
        return ruleActions.map(RuleMapper::toDto);
    }

    public List<OrderItem> calculateDiscounts(List<OrderItem> orderItems) {
        logger.info("Calculate discounts...");
        List<OrderItem> orderItemsWithDiscount = orderItems.stream()
                .filter(orderItem -> !orderItem.getItem().getName().equals("Vegetables"))
                .map(this::calculateDiscount)
                .collect(Collectors.toList());

        OrderItem maxWeightVegetables = orderItems.stream()
                .filter(orderItem -> orderItem.getItem().getName().equals("Vegetables"))
                .max(Comparator.comparing(OrderItem::getQuantity))
                .orElse(null);

        if (maxWeightVegetables != null) {
            final OrderItem maxWeightVegesWithDiscount = calculateDiscount(maxWeightVegetables);
            List<OrderItem> veges = orderItems.stream()
                    .filter(orderItem -> orderItem.getItem().getName().equals("Vegetables"))
                    .collect(Collectors.toList());
            veges.forEach(orderItem -> orderItem.setDiscount(maxWeightVegesWithDiscount.getDiscount()));
            orderItemsWithDiscount.addAll(veges);
        }

        return orderItemsWithDiscount;
    }

    private List<Rule> initializeRules() {
        List<RuleAction> ruleActions = this.ruleActionRepository.findAll();
        List<Rule> rules = new ArrayList<>();

        for (RuleAction ruleAction : ruleActions) {
            Rule rule = new Rule.Builder()
                    .withExpression(ExpressionParser.fromString(ruleAction.getRule()))
                    .withAction(ruleAction.getAction())
                    .build();
            rules.add(rule);
        }

        return rules;
    }

    private OrderItem calculateDiscount(OrderItem orderItem) {
        List<Rule> rules = initializeRules();
        for (Rule rule : rules) {
            rule.eval(orderItem);
        }
        return orderItem;
    }
}
