package com.grocerystop.onlinegrocerystore.repository;

import com.grocerystop.onlinegrocerystore.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void getItemByName() {
        Item item = itemRepository.findByName("Bread");

        assertThat(item).isNotNull();
        assertThat(item.getPrice()).isEqualTo(BigDecimal.valueOf(2.0).setScale(2));
    }
}
