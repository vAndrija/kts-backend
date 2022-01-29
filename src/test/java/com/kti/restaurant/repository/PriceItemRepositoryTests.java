package com.kti.restaurant.repository;


import com.kti.restaurant.model.PriceItem;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class PriceItemRepositoryTests {

    @Autowired
    private PriceItemRepository priceItemRepository;

    @ParameterizedTest
    @MethodSource("provideValidDatesForFindPriceItemForDate")
    public void findPriceItemForDate_ReturnsPriceItems(LocalDate date, int menuItemId, double expected) {
        PriceItem priceItem = priceItemRepository.findPriceItemForDate(date, menuItemId);
        assertEquals(expected, priceItem.getValue());
    }

    private static Stream<Arguments> provideValidDatesForFindPriceItemForDate() {
        return Stream.of(
                Arguments.of(LocalDate.parse("2021-11-19"), 1, Double.valueOf(180.00))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDatesForFindPriceItemForDate")
    public void findPriceItemForDateInvalid_ReturnsPriceItems(LocalDate date, int menuItemId, Object expected) {
        PriceItem priceItem = priceItemRepository.findPriceItemForDate(date, menuItemId);
        assertEquals(expected, priceItem);
    }

    private static Stream<Arguments> provideInvalidDatesForFindPriceItemForDate() {
        return Stream.of(
                Arguments.of(LocalDate.parse("2021-11-15"), 1, null),
                Arguments.of(LocalDate.parse("2021-11-19"), 100, null)
        );
    }

}