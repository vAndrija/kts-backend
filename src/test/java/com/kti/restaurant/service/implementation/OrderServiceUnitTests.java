package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.*;
import com.kti.restaurant.model.enums.OrderItemStatus;
import com.kti.restaurant.model.enums.OrderStatus;
import com.kti.restaurant.repository.OrderRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.repository.WaiterRepository;
import com.kti.restaurant.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    private WaiterService waiterService;
    @Mock
    private OrderItemService orderItemService;

    @Mock
    private OrderRepository orderRepository;

    @BeforeEach
    public void setup() throws Exception {
        Order order = new Order(OrderStatus.ORDERED, LocalDateTime.parse("2021-12-08T20:00"),
                1000.00, new RestaurantTable(), new Waiter());
        order.setId(1);
        Waiter waiter = new Waiter("Milic", "Sara", "0654123699",
                "saramilic@gmail.com", "02356987451");
        waiter.setId(7);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(waiterService.findById(7)).thenReturn(waiter);
    }

    @Test
    public void findById_ValidId_ReturnsExistingOrder() throws Exception {
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
    public void update_ValidId_ReturnsUpdatedOrder() throws Exception {
        Order orderForUpdate = new Order(OrderStatus.FINISHED, LocalDateTime.parse("2022-10-10T14:15"),
                3000.00, new RestaurantTable(), new Waiter());
        orderForUpdate.setId(1);

        when(orderRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));

        Order updatedOrder = orderService.update(orderForUpdate, 1);
        assertEquals(OrderStatus.FINISHED, updatedOrder.getStatus());
        assertEquals(LocalDateTime.parse("2022-10-10T14:15"), updatedOrder.getDateOfOrder());
        assertEquals(3000.00, updatedOrder.getPrice());

    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.update(null, 152);
        });

    }

    @Test
    public void delete_ValidId() {
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
    public void filterByStatus_ValidStatus_ReturnsExistingOrders() throws Exception {
        List<Order> filterByStatus = new ArrayList<>();
        Order order = new Order(OrderStatus.ORDERED, LocalDateTime.parse("2022-10-10T10:00"),
                1000.00, new RestaurantTable(), new Waiter());
        filterByStatus.add(order);
        when(orderRepository.findByWaiterAndStatus(7, OrderStatus.ORDERED, PageRequest.of(0, 8))).thenReturn(new PageImpl<>(filterByStatus));

        Page<Order> filtered = orderService.filterByStatus(7, "Poručeno", PageRequest.of(0, 8));
        assertEquals(1, filtered.getContent().size());
    }

    @Test
    public void filterByStatus_InvalidWaiterId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.filterByStatus(-1, "Poručeno", PageRequest.of(0, 8));
        });
    }

    @Test
    public void update_ValidStatus_ReturnsUpdatedOrder() throws Exception {
        Order orderForUpdate = new Order(OrderStatus.FINISHED, LocalDateTime.parse("2022-10-10T14:15"),
                3000.00, new RestaurantTable(), new Waiter());
        orderForUpdate.setId(5);

        when(orderRepository.save(any()))
                .thenAnswer(a -> a.getArgument(0));
        when(orderRepository.findById(5)).thenReturn(Optional.of(orderForUpdate));

        Order updatedOrder = orderService.updateStatus(5, "Plaćeno");
        assertEquals(OrderStatus.PAID, updatedOrder.getStatus());
        assertEquals(LocalDateTime.parse("2022-10-10T14:15"), updatedOrder.getDateOfOrder());
        assertEquals(3000.00, updatedOrder.getPrice());

    }

    @Test
    public void update_InvalidStatus_ThrowsBadLogicException() {
        assertThrows(BadLogicException.class, () -> {
            orderService.updateStatus(1, "Završeno");
        });

    }

    @Test
    public void update_InvalidStatus_ThrowsMissingEntityException() {
        when(orderItemService.checkIfServed(1)).thenReturn(true);
        assertThrows(MissingEntityException.class, () -> {
            orderService.updateStatus(1, " ");
        });

    }


    @Test
    public void findByWaiter_ValidWaiterId_ReturnsExistingOrders() throws Exception {
        List<Order> orders = new ArrayList<>();
        Order order = new Order(OrderStatus.ORDERED, LocalDateTime.parse("2022-10-10T10:00"),
                1000.00, new RestaurantTable(), new Waiter());
        orders.add(order);
        when(orderRepository.findByWaiter(7, PageRequest.of(0, 8))).thenReturn(new PageImpl<>(orders));

        Page<Order> filtered = orderService.findByWaiter(7, PageRequest.of(0, 8));
        assertEquals(1, filtered.getContent().size());
    }

    @Test
    public void findByWaiter_InvalidWaiterId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.findByWaiter(1, PageRequest.of(0, 8));
        });

    }

}
