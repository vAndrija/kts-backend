package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.model.Waiter;
import com.kti.restaurant.model.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class OrderServiceIntegrationTests {

    @Autowired
    private OrderService orderService;

    @Test
    public void findAll_NumberOfOrders() {
        List<Order> orders = orderService.findAll();

        assertEquals(3, orders.size());
    }

    @Test
    public void findById_ValidId_ExistingOrder() throws Exception {
        Order order = orderService.findById(1);

        assertEquals(OrderStatus.ORDERED, order.getStatus());
        assertEquals(1520, order.getPrice());
        assertEquals(LocalDateTime.parse("2021-11-19T14:15"), order.getDateOfOrder());

    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.findById(4);
        });
    }

    @Test
    @Rollback
    public void create_ValidOrder() throws Exception {
        Order orderForCreate = new Order(OrderStatus.ORDERED, LocalDateTime.parse("2021-12-08T11:20"),
                500.00, new RestaurantTable(), new Waiter());

        Order order = orderService.create(orderForCreate);
        assertEquals(OrderStatus.ORDERED, order.getStatus());
        assertEquals(500, order.getPrice());
        assertEquals(LocalDateTime.parse("2021-12-08T11:20"), order.getDateOfOrder());

    }

    @Test
    @Rollback
    public void update_ValidId_ExistingOrder() throws Exception {
        Order orderForUpdate = new Order(OrderStatus.PAID, LocalDateTime.parse("2021-12-01T18:20"),
                8500.00, new RestaurantTable(), new Waiter());

        Order updated = orderService.update(orderForUpdate, 1);
        assertEquals(OrderStatus.PAID, updated.getStatus());
        assertEquals(8500.00, updated.getPrice());
        assertEquals(LocalDateTime.parse("2021-12-01T18:20"), updated.getDateOfOrder());
    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.update(null, 152);
        });
    }

    @Test
    @Rollback
    public void delete_ValidId_OrderDeleted() throws Exception {
        orderService.delete(1);
        assertThrows(MissingEntityException.class, () -> {
            orderService.findById(1);
        });

    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.delete(152);
        });
    }

    @Test
    public void filterByStatus_ValidStatus_ExistingOrders() {
        Collection<Order> filteredOrders = orderService.filterByStatus("ORDERED");

        assertEquals(2, filteredOrders.size());
    }

    @Test
    public void filterByStatus_InvalidStatus_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.filterByStatus("PREPARATION");
        });
    }
}
