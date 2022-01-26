package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;

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

    @ParameterizedTest
    @MethodSource("provideSearchParams")
    public void search_SearchParams_ReturnsSetOfMenuItems(String searchParam, Integer sizeOfReturnedSet) {
        var menuItems = menuItemService.search(searchParam);
        assertEquals(sizeOfReturnedSet, menuItems.size());
    }

    private static Stream<Arguments> provideSearchParams() {
        return Stream.of(
                Arguments.of("limun", 1),
                Arguments.of("coca cola", 1),
                Arguments.of("dimljeni", 1),
                Arguments.of("Dezert", 3),
                Arguments.of("Hrana", 10),
                Arguments.of("abcd", 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideFilterParams")
    public void filter_Category_ReturnsSetOfMenuItems(String filterParam, Integer sizeOfReturnedSet) {
        var menuItems = menuItemService.filter(filterParam);
        assertEquals(sizeOfReturnedSet, menuItems.size());
    }

    private static Stream<Arguments> provideFilterParams() {
        return Stream.of(
                Arguments.of("Glavno jelo", 4),
                Arguments.of("abcd", 0)
        );
    }

    @ParameterizedTest
    @MethodSource("validMenuIdAndPageable")
    public void findByMenu_MenuIdPageable_ReturnsMenuItems(Integer menuId, Pageable pageable) throws Exception {
        Page<MenuItem> menuItems = menuItemService.findByMenu(menuId, pageable);
        assertEquals(pageable.getPageSize(), menuItems.getContent().size());
        assertEquals(menuId, menuItems.getContent().get(0).getMenu().getId());
    }

    private static Stream<Arguments> validMenuIdAndPageable() {
        return Stream.of(
                Arguments.of(1, PageRequest.of(0, 5)),
                Arguments.of(1, PageRequest.of(1, 5))
        );
    }

    @ParameterizedTest
    @MethodSource("invalidMenuIdAndValidPageable")
    public void findByMenu_InvalidMenuIdPageable_ThrowsMissingEntityException(Integer menuId, Pageable pageable) throws Exception {
        assertThrows(MissingEntityException.class, () -> {
            Page<MenuItem> menuItems = menuItemService.findByMenu(menuId, pageable);
        });
    }

    private static Stream<Arguments> invalidMenuIdAndValidPageable() {
        return Stream.of(
                Arguments.of(15, PageRequest.of(0, 5)),
                Arguments.of(15, PageRequest.of(1, 5))
        );
    }

    public void searchAndFilterMenuItems_MenuIdSearchParamFilterPageable_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            menuItemService.searchAndFilterMenuItems(12, "", "", PageRequest.of(0, 5));
        });
    }

    @ParameterizedTest
    @MethodSource("getMenuIdSearchParamFilterAndPageable")
    public void searchAndFilterMenuItems_MenuIdSearchParamFilterPageable_ReturnsMenuItems(Integer menuId, String searchParam,
                                                String category, Pageable page, Integer sizeofReturnedList, String name, Integer indexOfMenuItem) throws Exception {
        Page<MenuItem> menuItems = menuItemService.searchAndFilterMenuItems(menuId, searchParam, category, page);
        assertEquals(sizeofReturnedList, menuItems.getContent().size());
        assertEquals(name, menuItems.getContent().get(indexOfMenuItem).getName());
    }

    private static Stream<Arguments> getMenuIdSearchParamFilterAndPageable() {
        return Stream.of(
                Arguments.of(1, "", "", PageRequest.of(0, 5), 5, "coca cola", 0),
                Arguments.of(1, "cola", "", PageRequest.of(0, 5), 1, "coca cola", 0),
                Arguments.of(1, "cola", "Bezalkoholno piće", PageRequest.of(0, 5), 1, "coca cola", 0),
                Arguments.of(1, "", "Bezalkoholno piće", PageRequest.of(0, 5), 2, "limunada", 1)
        );
    }
}
