package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Menu;
import com.kti.restaurant.repository.MenuRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class MenuServiceUnitTests {

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenuRepository menuRepository;

    @BeforeEach
    public void setUp() {
        Menu menu = new Menu("Glavni meni", LocalDateTime.parse("2020-11-10T13:00"), LocalDateTime.parse("2022-11-10T13:00"), 1);

        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(menuRepository.findById(2)).thenThrow(MissingEntityException.class);
    }

    @Test
    public void findById_ValidId_ReturnsExistingMenu() throws Exception {
        Menu menu = menuService.findById(1);

        assertEquals("Glavni meni", menu.getName());
        assertEquals(LocalDateTime.parse("2020-11-10T13:00"), menu.getDurationStart());
        assertEquals(LocalDateTime.parse("2022-11-10T13:00"), menu.getDurationEnd());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuService.findById(2);
        });
    }

    @Test
    public void update_ValidId_ReturnsUpdatedMenu() throws Exception {
        Menu menuForUpdate = new Menu("GLAVNI MENI", LocalDateTime.parse("2021-11-10T13:00"), LocalDateTime.parse("2023-11-10T13:00"), 1);

        when(menuRepository.save(any())).thenAnswer(a -> a.getArgument(0));

        Menu menu = menuService.update(menuForUpdate, 1);

        assertEquals("GLAVNI MENI", menu.getName());
        assertEquals(LocalDateTime.parse("2021-11-10T13:00"), menu.getDurationStart());
        assertEquals(LocalDateTime.parse("2023-11-10T13:00"), menu.getDurationEnd());

        verify(menuRepository, times(1)).findById(1);
        verify(menuRepository, times(1)).save(any());
    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuService.update(null, 2);
        });

        verify(menuRepository, times(1)).findById(2);
        verify(menuRepository, times(0)).save(any());
    }

    @Test
    public void delete_ValidId() throws Exception {
        Assertions.assertDoesNotThrow(() -> {
            menuService.delete(1);
        });

        verify(menuRepository, times(1)).findById(1);
        verify(menuRepository, times(1)).deleteById(1);
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        Assertions.assertThrows(MissingEntityException.class, () -> {
            menuService.delete(2);
        });

        verify(menuRepository, times(1)).findById(2);
        verify(menuRepository, times(0)).deleteById(2);
    }
}
