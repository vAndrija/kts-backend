package com.kti.restaurant.repository;


import com.kti.restaurant.model.PriceItem;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class PriceItemRepositoryTests {

    @Autowired
    private PriceItemRepository priceItemRepository;
    
    @Test
    public void findPriceItemForDate_ValidDate_ExistingPriceItem() {
    	PriceItem priceItem = priceItemRepository.findPriceItemForDate(LocalDate.parse("2021-11-19"), 1);
    	assertEquals(Double.valueOf(180.00), priceItem.getValue());
    }
    
    @Test
    public void findPriceItemForDate_InvalidDate_Null() {
    	PriceItem priceItem = priceItemRepository.findPriceItemForDate(LocalDate.parse("2021-11-15"), 1);
    	assertEquals(null, priceItem);
    }
    
    @Test
    public void findPriceItemForDate_InvalidMenuItemId_Null() {
    	PriceItem priceItem = priceItemRepository.findPriceItemForDate(LocalDate.parse("2021-11-19"), 100);
    	assertEquals(null, priceItem);
    }
}