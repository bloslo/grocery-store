package com.grocerystop.onlinegrocerystore.repository;

import com.grocerystop.onlinegrocerystore.model.RuleAction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RuleActionRepositoryTest {

    @Autowired
    private RuleActionRepository ruleActionRepository;

    @Test
    void insertRuleAction() {
        RuleAction ruleAction = new RuleAction();
        ruleAction.setRule("name = 'Vegetables' and quantity > 100 and quantity <= 500");
        ruleAction.setAction("unitPrice * ( 7 / 100 )");

        RuleAction savedRuleAction = ruleActionRepository.save(ruleAction);

        assertThat(savedRuleAction).isNotNull();
        assertThat(savedRuleAction.getId()).isGreaterThan(0);
    }

    @Test
    void getRuleAction() {
        RuleAction ruleAction = new RuleAction();
        ruleAction.setRule("name = 'Vegetables' and quantity > 0 and quantity <= 100");
        ruleAction.setAction("unitPrice * ( 5 / 100 )");

        RuleAction savedRuleAction = ruleActionRepository.save(ruleAction);
        Optional<RuleAction> optionalRuleAction = ruleActionRepository.findById(1L);

        assertThat(optionalRuleAction).isPresent();
    }
}
