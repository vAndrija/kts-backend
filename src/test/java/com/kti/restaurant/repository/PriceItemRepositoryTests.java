package com.kti.restaurant.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.kti.restaurant.model.PriceItem;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;


@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class PriceItemRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PriceItemRepository priceItemRepository;
    
    @Test
    public void findPriceItemForDate_ValidDate_ExistingPriceItem() {
    	PriceItem priceItem = priceItemRepository.findPriceItemForDate(LocalDate.parse("2021-11-19"), 1);
    	assertEquals(new Double(180.00), priceItem.getValue());
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