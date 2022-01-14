package com.kti.restaurant.repository;

import com.kti.restaurant.model.Menu;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class MenuItemRepositoryTests {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @ParameterizedTest
    @MethodSource("validNameAndDescription")
    public void findByNameAndDescription_ParamNameDescription_ReturnsMenuItems(String searchParam, Integer expectedSize, String expectedMenuItemName) {
        List<MenuItem> menuItems = menuItemRepository.findByNameAndDescription(searchParam);
        assertEquals(expectedSize, menuItems.size());
        assertEquals(expectedMenuItemName, menuItems.get(0).getName());
    }

    private static Stream<Arguments> validNameAndDescription() {
        return Stream.of(
                Arguments.of("limun", 1, "limunada"),
                Arguments.of("coca", 1, "coca cola"),
                Arguments.of("dimljeni", 1, "dimljeni saran")
        );
    }

    @ParameterizedTest
    @MethodSource("validMenuItemCategory")
    public void findByCategory_Category_ReturnsMenuItems(MenuItemCategory category, Integer exceptedSize) {
        List<MenuItem> menuItems = menuItemRepository.findByCategory(category);
        assertEquals(exceptedSize, menuItems.size());
    }

    private static Stream<Arguments> validMenuItemCategory() {
        return Stream.of(
                Arguments.of(MenuItemCategory.DESSERT, 3)
        );
    }

    @ParameterizedTest
    @MethodSource("validMenuItemType")
    public void findByType_Type_ReturnsMenuItems(MenuItemType type, Integer exceptedSize) {
        List<MenuItem> menuItems = menuItemRepository.findByType(type);
        assertEquals(exceptedSize, menuItems.size());
    }

    private static Stream<Arguments> validMenuItemType() {
        return Stream.of(
                Arguments.of(MenuItemType.FOOD, 10)
        );
    }

    @ParameterizedTest
    @MethodSource("validMenuIdAndPageable")
    public void findByMenu_MenuId_Pageable_ReturnsMenuItems(Integer menuId, Pageable pageable) {
        Page<MenuItem> menuItems = menuItemRepository.findByMenu(menuId, pageable);
        assertEquals(pageable.getPageSize(), menuItems.getContent().size());
        assertEquals(menuId, menuItems.getContent().get(0).getMenu().getId());
    }

    private static Stream<Arguments> validMenuIdAndPageable() {
        return Stream.of(
                Arguments.of(1, PageRequest.of(0, 5)),
                Arguments.of(1, PageRequest.of(1, 5))
        );
    }
}
