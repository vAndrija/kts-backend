package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.PriceItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class PriceItemServiceIntegrationTests {
    @Autowired
    private PriceItemService priceItemService;

    @Test
    public void findAll_ReturnsExistingPriceItems() {
        List<PriceItem> priceItems = priceItemService.findAll();
        assertEquals(13, priceItems.size());
    }

    @Test
    public void findById_ValidPriceItemId_ReturnsExistingPriceItem() throws Exception {
        PriceItem priceItem = priceItemService.findById(1);

        assertEquals(Double.valueOf(180), priceItem.getValue());
        assertEquals(LocalDate.parse("2021-11-18"), priceItem.getStartDate());
        assertEquals(LocalDate.parse("2021-12-18"), priceItem.getEndDate());
        assertEquals(1, priceItem.getMenuItem().getId());
        assertEquals(true, priceItem.getCurrent());
        assertEquals(Double.valueOf(100), priceItem.getPreparationValue());
    }

    @Test
    public void findById_InvalidPriceItemId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            priceItemService.findById(20);
        });
    }

    @Rollback()
    @Test
    public void create_ValidPriceItem_ReturnsCreatedPriceItem() throws Exception {
        PriceItem priceItem = priceItemService.create(new PriceItem(Double.valueOf(350), LocalDate.parse("2021-11-15"), LocalDate.parse("2022-08-15"),
                new MenuItem(), true, Double.valueOf(240)));

        assertEquals(Double.valueOf(350), priceItem.getValue());
        assertEquals(LocalDate.now(), priceItem.getStartDate());
        assertEquals(null, priceItem.getEndDate());
        assertEquals(true, priceItem.getCurrent());
        assertEquals(Double.valueOf(240), priceItem.getPreparationValue());
    }

    @Rollback()
    @Test
    public void update_ValidPriceItem_ReturnsUpdatedPriceItem() throws Exception {
        PriceItem priceItemForUpdate = new PriceItem(Double.valueOf(350), null, null,
                null, false, Double.valueOf(240));

        PriceItem priceItem = priceItemService.update(priceItemForUpdate, 1);

        assertEquals(LocalDate.now(), priceItem.getEndDate());
        assertEquals(false, priceItem.getCurrent());
    }

    @Test
    public void update_InvalidPriceItem_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            priceItemService.update(null, 20);
        });
    }

    @Test
    public void delete_ValidPriceItemId() throws Exception {
        priceItemService.delete(1);
        assertThrows(MissingEntityException.class, () -> {
            priceItemService.findById(1);
        });
    }

    @Test
    public void delete_InvalidPriceItemId() {
        assertThrows(MissingEntityException.class, () -> {
            priceItemService.delete(20);
        });
    }

    @Test
    public void findPriceForDate_ValidDateValidMenuItemId_ReturnsExistingPriceItem() throws Exception {
        PriceItem priceItem = priceItemService.findPriceForDate(LocalDate.parse("2021-11-20"),1);

        assertEquals(Double.valueOf(180), priceItem.getValue());
        assertEquals(LocalDate.parse("2021-11-18"), priceItem.getStartDate());
        assertEquals(LocalDate.parse("2021-12-18"), priceItem.getEndDate());
        assertEquals(true, priceItem.getCurrent());
        assertEquals(Double.valueOf(100), priceItem.getPreparationValue());
    }

    @Test
    public void findPriceForDate_InvalidDateValidMenuItemId_ReturnsNull() throws Exception {
        PriceItem priceItem = priceItemService.findPriceForDate(LocalDate.parse("2022-11-18"), 1);

        assertNull(priceItem);
    }

    @Test
    public void findPriceForDate_ValidDateInvalidMenuItemId_ReturnsNull() throws Exception {
        PriceItem priceItem = priceItemService.findPriceForDate(LocalDate.parse("2021-11-20"), 20);

        assertNull(priceItem);
    }

    @Test
    public void findPriceForDate_InvalidDateInvalidMenuItemId_ReturnsNull() {
        PriceItem priceItem = priceItemService.findPriceForDate(LocalDate.parse("2022-11-20"), 20);

        assertNull(priceItem);
    }
}
