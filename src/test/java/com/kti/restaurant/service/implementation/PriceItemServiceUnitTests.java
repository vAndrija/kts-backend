package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.PriceItem;
import com.kti.restaurant.repository.PriceItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class PriceItemServiceUnitTests {

    @InjectMocks
    private PriceItemService priceItemService;

    @Mock
    private PriceItemRepository priceItemRepository;

    @BeforeEach
    public void setUp() {
        PriceItem priceItem = new PriceItem(Double.valueOf(300), LocalDate.parse("2021-08-15"), LocalDate.parse("2022-08-15"),
                null, true, Double.valueOf(200));
        priceItem.setId(1);

        when(priceItemRepository.findById(1)).thenReturn(Optional.of(priceItem));
    }

    @Test
    public void findById_ValidPriceItemId_ExistingPriceItem() throws Exception {
        PriceItem priceItem = priceItemService.findById(1);

        assertEquals(Double.valueOf(300), priceItem.getValue());
        assertEquals(LocalDate.parse("2021-08-15"), priceItem.getStartDate());
        assertEquals(LocalDate.parse("2022-08-15"), priceItem.getEndDate());
        assertEquals(true, priceItem.getCurrent());
        assertEquals(Double.valueOf(200), priceItem.getPreparationValue());
        assertEquals(1, priceItem.getId());
    }

    @Test
    public void findById_InvalidPriceItemId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            priceItemService.findById(2);
        });
    }

    @Test
    public void update_ValidPriceItem_UpdatedPriceItem() throws Exception {
        PriceItem priceItemForUpdate = new PriceItem(Double.valueOf(350), LocalDate.parse("2021-11-15"), LocalDate.parse("2022-08-15"),
                null, true, Double.valueOf(240));

        when(priceItemRepository.save(any())).thenAnswer(a -> a.getArgument(0));

        PriceItem priceItem = priceItemService.update(priceItemForUpdate, 1);

        assertEquals(Double.valueOf(350), priceItem.getValue());
        assertEquals(LocalDate.parse("2021-11-15"), priceItem.getStartDate());
        assertEquals(LocalDate.parse("2022-08-15"), priceItem.getEndDate());
        assertEquals(true, priceItem.getCurrent());
        assertEquals(Double.valueOf(240), priceItem.getPreparationValue());
        assertEquals(1, priceItem.getId());
    }

    @Test
    public void update_InvalidPriceItemId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            priceItemService.update(null, 2);
        });
    }

    @Test
    public void delete_ValidPriceItemId_DeletedPriceItem() throws Exception {
        Assertions.assertDoesNotThrow(() -> {
            priceItemService.delete(1);
        });
        verify(priceItemRepository, times(1)).findById(1);
        verify(priceItemRepository, times(1)).deleteById(1);
    }

    @Test
    public void delete_InvalidPriceItemId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            priceItemService.delete(2);
        });
        verify(priceItemRepository, times(1)).findById(2);
        verify(priceItemRepository, times(0)).deleteById(2);
    }
}
