package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class MenuItemServiceIntegrationTests {
    @Autowired
    private MenuItemService menuItemService;

    @Test
    public void findAll_ReturnsExistingMenuItems() {
        List<MenuItem> menuItems = menuItemService.findAll();
        assertEquals(14, menuItems.size());
    }

    @Test
    public void findById_ValidId_ReturnsExistingDiscount() throws Exception {
        MenuItem menuItem = menuItemService.findById(1);
        assertEquals("coca cola", menuItem.getName());
        assertEquals("bezalkoholno gazirano pice", menuItem.getDescription());
        assertEquals(MenuItemType.DRINK, menuItem.getType());
        assertEquals(MenuItemCategory.NON_ALCOHOLIC, menuItem.getCategory());
        assertEquals(1, menuItem.getMenu().getId());
        assertEquals(true, menuItem.getAccepted());
        assertEquals(2, menuItem.getPreparationTime());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuItemService.findById(20);
        });
    }

    @Test
    public void create_ValidMenuItem_ReturnsCreatedMenuItem() throws Exception {
        MenuItem menuItem = menuItemService.create(new MenuItem("Coca-cola", "Gazirano pice", MenuItemCategory.NON_ALCOHOLIC,
                MenuItemType.DRINK, 2));
        assertEquals("Coca-cola", menuItem.getName());
        assertEquals("Gazirano pice", menuItem.getDescription());
        assertEquals(MenuItemCategory.NON_ALCOHOLIC, menuItem.getCategory());
        assertEquals(MenuItemType.DRINK, menuItem.getType());
    }

    @Test
    public void update_ValidId_ReturnsUpdatedMenuItem() throws Exception {
        MenuItem menuItem = new MenuItem("Coca-cola", "Gazirano pice", MenuItemCategory.NON_ALCOHOLIC,
                MenuItemType.DRINK, 5);
        menuItem.setId(1);

        MenuItem updatedMenuItem = menuItemService.update(menuItem, 1);

        assertEquals("Coca-cola", updatedMenuItem.getName());
        assertEquals("Gazirano pice", updatedMenuItem.getDescription());
        assertEquals(MenuItemCategory.NON_ALCOHOLIC, updatedMenuItem.getCategory());
        assertEquals(MenuItemType.DRINK, updatedMenuItem.getType());
        assertEquals(5, updatedMenuItem.getPreparationTime());
    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuItemService.update(null, 20);
        });
    }

    @Rollback()
    @Test
    public void delete_ValidId() throws Exception {
        menuItemService.delete(1);
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuItemService.findById(1);
        });
    }

    @Rollback()
    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() throws Exception {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuItemService.delete(20);
        });
    }

    @Test
    public void search_SearchParamNameDescription_ReturnsSetOfMenuItems() {
        var menuItems = menuItemService.search("limun");
        assertEquals(1, menuItems.size());
    }

    @Test
    public void search_SearchParamName_ReturnsSetOfMenuItems() {
        var menuItems = menuItemService.search("coca cola");
        assertEquals(1, menuItems.size());
    }

    @Test
    public void search_SearchParamDescription_ReturnsSetOfMenuItems() {
        var menuItems = menuItemService.search("dimljeni");
        assertEquals(1, menuItems.size());
    }

    @Test
    public void search_SearchParamCategory_ReturnsSetOfMenuItems() {
        var menuItems = menuItemService.search("Dezert");
        assertEquals(4, menuItems.size());
    }

    @Test
    public void search_SearchParamType_ReturnsSetOfMenuItems() {
        var menuItems = menuItemService.search("Hrana");
        assertEquals(10, menuItems.size());
    }

    @Test
    public void search_InvalidParam_ReturnsEmptySet() {
        var menuItems = menuItemService.search("abcd");
        assertEquals(0, menuItems.size());
    }

    @Test
    public void filter_ValidCategory_ReturnsSetOfMenuItems() {
        var menuItems = menuItemService.filter("Glavno jelo");
        assertEquals(4, menuItems.size());
    }

    @Test
    public void filter_InvalidCategory_ReturnsEmptySet() {
        var menuItems = menuItemService.filter("abcd");
        assertEquals(0, menuItems.size());
    }
}
