package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Discount;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class DiscountServiceIntegrationTests {
    @Autowired
    private DiscountService discountService;

    @Test
    public void findAll_ReturnsExistingDiscounts() {
        List<Discount> discounts = discountService.findAll();
        assertEquals(1, discounts.size());
    }

    @Test
    public void findById_ValidId_ReturnsExistingDiscount() throws Exception {
        Discount discount = discountService.findById(1);
        assertEquals(10, discount.getValue());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            discountService.findById(2);
        });
    }

    @Test
    public void create_ValidDiscount_ReturnsCreatedDiscount() throws Exception {
        Discount discount = discountService.create(new Discount(15, LocalDate.parse("2021-05-11"),
                LocalDate.parse("2021-11-11"), false, null));
        assertEquals(15, discount.getValue());
        assertEquals(LocalDate.parse("2021-05-11"), discount.getStartDate());
        assertEquals(LocalDate.parse("2021-11-11"), discount.getEndDate());
        assertEquals(false, discount.getCurrent());
    }

    @Test
    public void create_DiscountWithInvalidDates_ThrowBadLogicException() {
        assertThrows(BadLogicException.class, () -> {
            discountService.create(new Discount(15, LocalDate.parse("2021-11-11"),
                    LocalDate.parse("2021-05-11"), false, null));
        });
    }

    @Test
    void update_ValidId_ReturnsUpdatedDiscount() throws Exception {
        Discount discount = new Discount(25, LocalDate.parse("2021-05-11"),
                LocalDate.parse("2021-11-11"), false, null);
        discount.setId(1);

        Discount updatedDiscount = discountService.update(discount, 1);

        assertEquals(25, updatedDiscount.getValue());
        assertEquals(LocalDate.parse("2021-05-11"), updatedDiscount.getStartDate());
        assertEquals(LocalDate.parse("2021-11-11"), updatedDiscount.getEndDate());
        assertEquals(false, updatedDiscount.getCurrent());
    }

    @Test
    void update_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            discountService.update(null, 10);
        });
    }

    @Test
    void update_InvalidDates_ThrowsBadLogicException() {
        Assertions.assertThrows(BadLogicException.class, () -> {
            discountService.update(new Discount(null, LocalDate.parse("2022-11-05"), LocalDate.parse("2021-11-05"), null, null), 1);
        });
    }

    @Rollback()
    @Test
    void delete_ValidId() throws Exception {
        discountService.delete(1);
        Assertions.assertThrows(MissingEntityException.class, () -> {
            discountService.findById(1);
        });
    }

    @Rollback()
    @Test
    void delete_InvalidId_ThrowsMissingEntityException() throws Exception {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            discountService.delete(10);
        });
    }
}
