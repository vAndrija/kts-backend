package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.model.enums.OrderItemStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class OrderItemServiceIntegrationTests {

    @Autowired
    private OrderItemService orderItemService;


    @Test
    public void findAll_ReturnsExistingOrderItems() {
        List<OrderItem> orderItems = orderItemService.findAll();

        assertEquals(orderItems.size(), 11);
    }

    @Test
    public void findById_ReturnsExistingOrderItem() throws Exception {
        OrderItem orderItem = orderItemService.findById(1);

        assertEquals("", orderItem.getNote());
        assertEquals(OrderItemStatus.ORDERED, orderItem.getStatus());
        assertEquals(1, orderItem.getPriority());
        assertEquals(2, orderItem.getQuantity());

    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderItemService.findById(100);
        });
    }

    @Test
    @Rollback
    public void create_ValidOrderItem_ReturnsCreatedOrderItem() throws Exception {
        OrderItem orderItemForCreate = new OrderItem(1, "bez secera", OrderItemStatus.ORDERED, 1,
                new MenuItem());

        OrderItem orderItem = orderItemService.create(orderItemForCreate);

        assertEquals(1, orderItem.getQuantity());
        assertEquals(1, orderItem.getPriority());
        assertEquals("bez secera", orderItem.getNote());
        assertEquals(OrderItemStatus.ORDERED, orderItem.getStatus());
    }

    @Test
    @Rollback
    public void update_ValidId_ReturnsUpdatedOrderItem() throws Exception {
        OrderItem orderItemForUpdate = new OrderItem(2, "bez luka", OrderItemStatus.ORDERED, 1,
                new MenuItem());

        OrderItem updatedOrder = orderItemService.update(orderItemForUpdate, 1);

        assertEquals(1, updatedOrder.getPriority());
        assertEquals(2, updatedOrder.getQuantity());
        assertEquals("bez luka", updatedOrder.getNote());
        assertEquals(OrderItemStatus.ORDERED, updatedOrder.getStatus());

    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderItemService.update(null, 100);
        });
    }

    @Test
    @Rollback
    public void delete_ValidId() throws Exception {
        orderItemService.delete(1);
        assertThrows(MissingEntityException.class, () -> {
            orderItemService.findById(1);
        });

    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderItemService.delete(100);
        });
    }

    @Test
    public void findOrderItemsInPeriod_ValidPeriod_ReturnsExistingOrderItems() {
        List<OrderItem> orderItems = orderItemService.findOrderItemsInPeriod(LocalDateTime.parse("2022-11-18T08:00"),
                LocalDateTime.parse("2022-11-19T08:00"));

        assertEquals(2, orderItems.size());
    }

    @Test
    public void findOrderItemsInPeriod_InvalidPeriod_ReturnsEmptyList() {
        List<OrderItem> orderItems = orderItemService.findOrderItemsInPeriod(LocalDateTime.parse("2022-11-18T14:00"),
                LocalDateTime.parse("2021-01-01T14:00"));

        assertEquals(0, orderItems.size());
    }

    @Test
    public void findOrderItemsInPeriodForMenuItem_ValidPeriodValidMenuItemId_ReturnsExistingOrderItems() {
        List<OrderItem> orderItems = orderItemService.findOrderItemsInPeriodForMenuItem(LocalDateTime.parse("2021-11-18T08:00"),
                LocalDateTime.parse("2021-11-19T23:00"), 1);

        assertEquals(2, orderItems.size());
    }

    @Test
    public void findOrderItemsInPeriodForMenuItem_InvalidPeriodValidMenuItemId_ReturnsEmptyList() {
        List<OrderItem> orderItems = orderItemService.findOrderItemsInPeriodForMenuItem(LocalDateTime.parse("2022-11-18T08:00"),
                LocalDateTime.parse("2022-11-01T23:00"), 1);

        assertEquals(0, orderItems.size());
    }


    @Test
    public void findOrderItemsInPeriodForMenuItem_InvalidPeriodInvalidMenuItemId_ReturnsEmptyList() {
        List<OrderItem> orderItems = orderItemService.findOrderItemsInPeriodForMenuItem(LocalDateTime.parse("2022-11-18T08:00"),
                LocalDateTime.parse("2022-11-01T23:00"), 100);

        assertEquals(0, orderItems.size());
    }

    @Test
    public void findByCook_ValidPeriodValidCookId_ReturnsExistingOrderItems() {
        List<OrderItem> orderItems = orderItemService.findByCook(4, LocalDateTime.parse("2025-11-18T08:00"),
                LocalDateTime.parse("2021-11-19T23:00"));
        assertEquals(orderItems.size(), 0);
    }

    @Test
    public void findByCook_InvalidPeriodValidCookId_ReturnsEmptyList() {
        List<OrderItem> orderItems = orderItemService.findByCook(4, LocalDateTime.parse("2025-11-18T08:00"),
                LocalDateTime.parse("2021-11-19T23:00"));
        assertEquals(orderItems.size(), 0);
    }

    @Test
    public void findByCook_ValidPeriodInvalidCookId_ReturnsEmptyList() {
        List<OrderItem> orderItems = orderItemService.findByCook(250, LocalDateTime.parse("2021-11-18T08:00"),
                LocalDateTime.parse("2021-11-19T23:00"));
        assertEquals(orderItems.size(), 0);
    }

    @Test
    public void findByBartender_ValidPeriodValidBartenderId_ReturnsExistingOrderItems() {
        List<OrderItem> orderItems = orderItemService.findByBartender(2, LocalDateTime.parse("2022-11-18T08:00"),
                LocalDateTime.parse("2022-11-28T23:00"));
        assertEquals(orderItems.size(), 1);
    }

    @Test
    public void findByBartender_InvalidPeriodValidBartenderId_ReturnsEmptyList() {
        List<OrderItem> orderItems = orderItemService.findByBartender(2, LocalDateTime.parse("2022-12-22T08:00"),
                LocalDateTime.parse("2022-11-01T11:45"));
        assertEquals(orderItems.size(), 0);
    }

    @Test
    public void findByBartender_InvalidPeriodInvalidBartenderId_ReturnsEmptyList() {
        List<OrderItem> orderItems = orderItemService.findByBartender(1500, LocalDateTime.parse("2025-10-22T08:00"),
                LocalDateTime.parse("2022-01-01T11:45"));
        assertEquals(orderItems.size(), 0);
    }

    @Test
    public void findByEmployee_ValidId_ExistingOrderItems() {
        Page<OrderItem> orderItems = orderItemService.findByEmployee(PageRequest.of(0, 5), 3);
        assertEquals(orderItems.getContent().size(), 2);
    }

    @Test
    public void findByEmployee_InvalidId_EmptyList() {
        Page<OrderItem> orderItems = orderItemService.findByEmployee(PageRequest.of(0, 5), 1);
        assertEquals(orderItems.getContent().size(), 0);
    }

    @Test
    public void findByEmployeeAndStatus_ValidId_ExistingOrderItems() {
        Page<OrderItem> orderItems = orderItemService.findByEmployeeAndStatus(4, "U pripremi", PageRequest.of(0, 5));
        assertEquals(orderItems.getContent().size(), 2);
    }

    @Test
    public void findByEmployeeAndStatus_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderItemService.findByEmployeeAndStatus(-4, "U pripremi", PageRequest.of(0, 5));
        });
    }

    @Test
    public void findByEmployeeAndStatus_ValidStatus_ExistingOrderItems() {
        Page<OrderItem> orderItems = orderItemService.findByEmployeeAndStatus(3, "Pripremljeno", PageRequest.of(0, 5));
        assertEquals(orderItems.getContent().size(), 1);
    }

    @Test
    public void findByEmployeeAndStatus_InvalidStatus_ThrowsBadLogicException() {
        assertThrows(BadLogicException.class, () -> {
            orderItemService.findByEmployeeAndStatus(4, " ", PageRequest.of(0, 5));
        });
    }


    @Test
    @Rollback
    public void updateStatus_ValidId_ExistingOrderItem() throws Exception {
        OrderItem updatedOrder = orderItemService.updateStatus(4, "Pripremljeno");

        assertEquals(1, updatedOrder.getPriority());
        assertEquals(1, updatedOrder.getQuantity());
        assertEquals("", updatedOrder.getNote());
        assertEquals(OrderItemStatus.PREPARED, updatedOrder.getStatus());

    }

    @Test
    public void updateStatus_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderItemService.updateStatus(0, "Pripremljeno");
        });
    }
}
