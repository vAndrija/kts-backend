package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Menu;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import com.kti.restaurant.repository.MenuItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class MenuItemServiceUnitTests {

    @InjectMocks
    private MenuItemService menuItemService;

    @Mock
    private MenuItemRepository menuItemRepository;

    @BeforeEach
    public void setUp() {
        MenuItem menuItem = new MenuItem("Coca-cola", "Gazirano pice", MenuItemCategory.NON_ALCOHOLIC, MenuItemType.DRINK, 2);
        menuItem.setId(1);

        when(menuItemRepository.findById(1)).thenReturn(Optional.of(menuItem));
    }

    @Test
    public void findById_ValidId_ExistingMenuItem() throws Exception {
        MenuItem menuItem = menuItemService.findById(1);
        assertEquals("Coca-cola", menuItem.getName());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuItemService.findById(20);
        });
    }

    @Test
    public void delete_ValidId_MenuItemDeleted() {
        Assertions.assertDoesNotThrow(() -> {
            menuItemService.delete(1);
        });
        verify(menuItemRepository, times(1)).findById(1);
        verify(menuItemRepository, times(1)).deleteById(1);
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuItemService.delete(20);
        });
        verify(menuItemRepository, times(1)).findById(20);
        verify(menuItemRepository, times(0)).deleteById(20);
    }

    @Test
    public void update_ValidMenuItemId_ValidItem() throws Exception {
        MenuItem menuItemForUpdate = new MenuItem("COCA-COLA", "Gazirano bezalhoholno pice", MenuItemCategory.NON_ALCOHOLIC, MenuItemType.DRINK,
                3);

        when(menuItemRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));

        MenuItem menuItem = menuItemService.update(menuItemForUpdate, 1);

        assertEquals("COCA-COLA", menuItem.getName());
        assertEquals("Gazirano bezalhoholno pice", menuItem.getDescription());
        assertEquals(3, menuItem.getPreparationTime());
        verify(menuItemRepository, times(1)).findById(1);
        verify(menuItemRepository, times(1)).save(any());
    }

    @Test
    public void update_InvalidMenuItemId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuItemService.update(null, 20);
        });
        verify(menuItemRepository, times(1)).findById(20);
        verify(menuItemRepository, times(0)).save(any());
    }
}
