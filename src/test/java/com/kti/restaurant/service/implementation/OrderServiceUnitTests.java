package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.model.Waiter;
import com.kti.restaurant.model.enums.OrderStatus;
import com.kti.restaurant.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class OrderServiceUnitTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        Order order = new Order(OrderStatus.ORDERED, LocalDateTime.parse("2021-12-08T20:00"),
                1000.00, new RestaurantTable(), new Waiter());
        order.setId(1);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

    }

    @Test
    public void findById_ValidId_ExistingOrder() throws Exception {
        Order order = orderService.findById(1);

        assertEquals(OrderStatus.ORDERED, order.getStatus());
        assertEquals(LocalDateTime.parse("2021-12-08T20:00"), order.getDateOfOrder());
        assertEquals(1000.00, order.getPrice());

    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.findById(890);
        });
    }

    @Test
    public void update_ValidId_ExistingOrder() throws Exception {
        Order orderForUpdate = new Order(OrderStatus.FINISHED, LocalDateTime.parse("2022-10-10T14:15"),
                3000.00, new RestaurantTable(), new Waiter());
        orderForUpdate.setId(1);

        when(orderRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));

        Order updatedOrder = orderService.update(orderForUpdate, 1);
        assertEquals(OrderStatus.FINISHED, updatedOrder.getStatus());
        assertEquals(updatedOrder.getDateOfOrder(), LocalDateTime.parse("2022-10-10T14:15"));
        assertEquals(updatedOrder.getPrice(), 3000.00);

    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.update(null, 152);
        });

    }

    @Test
    public void delete_ValidId_OrderDeleted() {
        assertDoesNotThrow(() -> {
            orderService.delete(1);
        });

        verify(orderRepository, times(1)).findById(1);
        verify(orderRepository, times(1)).deleteById(1);
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.delete(554);
        });

        verify(orderRepository, times(1)).findById(554);
        verify(orderRepository, times(0)).deleteById(554);
    }

    @Test
    public void filterByStatus_ValidStatus_ExistingOrders() {
        List<Order> filterByStatus = new ArrayList<>();
        Order order1 = new Order(OrderStatus.ORDERED, LocalDateTime.parse("2022-10-10T10:00"),
                1000.00, new RestaurantTable(), new Waiter());
        Order order2 = new Order(OrderStatus.FINISHED, LocalDateTime.parse("2022-10-10T14:15"),
                3000.00, new RestaurantTable(), new Waiter());
        filterByStatus.add(order1);

        when(orderRepository.findOrderByStatus(any())).thenReturn(filterByStatus);

        Collection<Order> filtered = orderService.filterByStatus("ORDERED");
        assertEquals(1, filtered.size());
    }

    @Test
    public void filterByStatus_InvalidStatus_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.filterByStatus("PREPARED");
        });
    }
}
