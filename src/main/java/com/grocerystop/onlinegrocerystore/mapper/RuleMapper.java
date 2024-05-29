package com.grocerystop.onlinegrocerystore.mapper;

import com.grocerystop.onlinegrocerystore.dto.RuleDto;
import com.grocerystop.onlinegrocerystore.model.RuleAction;

public class RuleMapper {
    public static RuleDto toDto(RuleAction ruleAction) {
        return new RuleDto(ruleAction.getDescription(), ruleAction.getRule());
    }
}
