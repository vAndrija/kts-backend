package com.kti.restaurant.repository;

import com.kti.restaurant.model.Menu;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class MenuRepositoryTests {

    @Autowired
    private MenuRepository menuRepository;

    @ParameterizedTest
    @MethodSource("findActiveMenus")
    public void findMenusForDate_Date_ReturnsMenus(LocalDateTime date, Integer expectedSize) {
        List<Menu> menuItems = menuRepository.findMenusForDate(date);
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
