package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Menu;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class MenuServiceIntegrationTests {
    @Autowired
    private MenuService menuService;

    @Test
    public void findAll_ListOfMenues() {
        List<Menu> menues = menuService.findAll();

        assertEquals(2, menues.size());
    }

    @Test
    public void findById_ValidId_ReturnsExistingMenu() throws Exception {
        Menu menu = menuService.findById(1);

        assertEquals("standardni", menu.getName());
        assertEquals(LocalDateTime.parse("2021-11-18T08:00"), menu.getDurationStart());
        assertEquals(LocalDateTime.parse("2022-11-18T08:00"), menu.getDurationEnd());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuService.findById(3);
        });
    }

    @Test
    public void create_ValidMenu_ReturnsCreatedMenu() throws Exception {
        Menu menu = menuService.create(new Menu("Glavni meni", LocalDateTime.parse("2021-10-10T13:00"),
                LocalDateTime.parse("2022-10-10T13:00")));

        assertEquals("Glavni meni", menu.getName());
        assertEquals(LocalDateTime.parse("2021-10-10T13:00"), menu.getDurationStart());
        assertEquals(LocalDateTime.parse("2022-10-10T13:00"), menu.getDurationEnd());
    }

    @Rollback()
    @Test
    public void update_ValidMenuId_ReturnsUpdatedMenu() throws Exception {
        Menu menuForUpdate = new Menu("Glavni meni", LocalDateTime.parse("2021-10-10T13:00"),
                LocalDateTime.parse("2022-10-10T13:00"), 1);

        Menu menu = menuService.update(menuForUpdate, 1);
        assertEquals("Glavni meni", menu.getName());
        assertEquals(LocalDateTime.parse("2021-10-10T13:00"), menu.getDurationStart());
        assertEquals(LocalDateTime.parse("2022-10-10T13:00"), menu.getDurationEnd());
    }

    @Test
    public void update_InvalidMenuId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuService.update(null, 3);
        });
    }

    @Rollback()
    @Test
    public void delete_ValidMenuId() throws Exception {
        menuService.delete(1);
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuService.findById(1);
        });
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuService.delete(3);
        });
    }

    @ParameterizedTest
    @MethodSource("findActiveMenus")
    public void findMenusForDate_Date_ReturnsMenus(LocalDateTime date, Integer expectedSize) {
        List<Menu> menuItems = menuService.findMenusForDate(date);
        assertEquals(expectedSize, menuItems.size());
    }

    private static Stream<Arguments> findActiveMenus() {
        return Stream.of(
                Arguments.of(LocalDateTime.of(2022, 1, 15, 14, 0), 1),
                Arguments.of(LocalDateTime.of(2022, 8, 15, 14, 0), 1),
                Arguments.of(LocalDateTime.of(2024, 1, 15, 14, 0), 0)
        );
    }
}
