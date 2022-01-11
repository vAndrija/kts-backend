package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.model.Waiter;
import com.kti.restaurant.model.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public void findAll_ReturnsExistingOrders() {
        List<Order> orders = orderService.findAll();

        assertEquals(3, orders.size());
    }

    @Test
    public void findById_ValidId_ReturnsExistingOrder() throws Exception {
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
    public void create_ValidOrder_ReturnsCreatedOrder() throws Exception {
        Order orderForCreate = new Order(OrderStatus.ORDERED, LocalDateTime.parse("2021-12-08T11:20"),
                500.00, new RestaurantTable(), new Waiter());

        Order order = orderService.create(orderForCreate);
        assertEquals(OrderStatus.ORDERED, order.getStatus());
        assertEquals(500, order.getPrice());
        assertEquals(LocalDateTime.parse("2021-12-08T11:20"), order.getDateOfOrder());

    }

    @Test
    @Rollback
    public void update_ValidId_ReturnsUpdatedOrder() throws Exception {
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
    public void delete_ValidId() throws Exception {
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
    public void filterByStatus_ValidStatus_ReturnsExistingOrders() throws Exception {
        Page<Order> filteredOrders = orderService.filterByStatus(7, "Poručeno", PageRequest.of(0, 8));
        assertEquals(2, filteredOrders.getContent().size());
    }

    @Test
    public void filterByStatus_InvalidStatus_ThrowsBadLogicException() {
        assertThrows(BadLogicException.class, () -> {
            orderService.filterByStatus(7, " ", PageRequest.of(0, 8));
        });
    }

    @Test
    public void filterByStatus_InvalidWaiterId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.filterByStatus(70, "Poručeno", PageRequest.of(0, 8));
        });
    }

    @Test
    public void findByWaiter_ValidWaiterId_ReturnsExistingOrders() throws Exception {
        Page<Order> orders = orderService.findByWaiter(7, PageRequest.of(0, 8));
        assertEquals(2, orders.getContent().size());
    }

    @Test
    public void findByWaiter_InvalidWaiterId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.findByWaiter(-1, PageRequest.of(0, 8));
        });
    }

    @Test
    @Rollback
    public void updateStatus_ValidStatus_ReturnsUpdatedOrder() throws Exception {
        Order order = orderService.updateStatus(1, "Završeno");
        assertEquals(OrderStatus.FINISHED, order.getStatus());
    }

    @Test
    public void updateStatus_InvalidStatus_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.updateStatus(4, " ");
        });
    }

    @Test
    public void updateStatus_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderService.updateStatus(-1, "Poručeno");
        });
    }

}
