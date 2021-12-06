package com.kti.restaurant.repository;


import static com.kti.restaurant.constants.PriceItemConstants.PRICE_ITEM_VALUE;
import static org.junit.Assert.assertEquals;

import com.kti.restaurant.model.PriceItem;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class PriceItemRepositoryTests {

    @Autowired
    private PriceItemRepository priceItemRepository;
    
    @Test
    public void findPriceItemForDate_ValidDate_ExistingPriceItem() {
    	PriceItem priceItem = priceItemRepository.findPriceItemForDate(LocalDate.parse("2021-11-19"), 1);
    	assertEquals(PRICE_ITEM_VALUE, priceItem.getValue());
    }
    
    @Test
    public void findPriceItemForDate_InvalidDate_Null() {
    	PriceItem priceItem = priceItemRepository.findPriceItemForDate(LocalDate.parse("2021-11-15"), 1);
    	assertEquals(priceItem, null);
    }
    
    @Test
    public void findPriceItemForDate_InvalidMenuItemKey_Null() {
    	PriceItem priceItem = priceItemRepository.findPriceItemForDate(LocalDate.parse("2021-11-19"), 100);
    	assertEquals(priceItem, null);
    }
}