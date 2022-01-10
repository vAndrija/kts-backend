package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.*;
import com.kti.restaurant.model.enums.OrderItemStatus;
import com.kti.restaurant.model.enums.OrderStatus;
import com.kti.restaurant.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class OrderItemServiceUnitTests {

    @InjectMocks
    OrderItemService orderItemService;

    @Mock
    OrderItemRepository orderItemRepository;

    @BeforeEach
    public void setup() {
        OrderItem orderItem = new OrderItem(1, "",
                OrderItemStatus.ORDERED, 1, new Order(OrderStatus.ORDERED, 300.00), new MenuItem(), new Bartender(), null);
        orderItem.setId(1);
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(orderItem));
    }

    @Test
    public void findById_ValidId_ReturnsExistingOrderItem() throws Exception {
        OrderItem orderItem = orderItemService.findById(1);
        assertEquals(1, orderItem.getId());
        assertEquals("", orderItem.getNote());
        assertEquals(1, orderItem.getPriority());
        assertEquals(OrderItemStatus.ORDERED, orderItem.getStatus());
        assertEquals(1, orderItem.getQuantity());
    }

    @Test
    public void findById_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderItemService.findById(2);
        });
    }

    @Test
    public void update_ValidId_ReturnsUpdatedOrderItem() throws Exception {
        OrderItem orderItemForUpdate = new OrderItem(2, "bez bibera", OrderItemStatus.PREPARATION,
                2, new Order(), new MenuItem(), null, new Cook());
        orderItemForUpdate.setId(1);

        when(orderItemRepository.save(any())).thenAnswer(a -> a.getArgument(0));

        OrderItem updatedOrderItem = orderItemService.update(orderItemForUpdate, 1);
        assertEquals(2, updatedOrderItem.getPriority());
        assertEquals(2, updatedOrderItem.getQuantity());
        assertEquals(OrderItemStatus.PREPARATION, updatedOrderItem.getStatus());
        assertEquals("bez bibera", updatedOrderItem.getNote());
    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderItemService.update(null, 100);
        });
    }

    @Test
    public void delete_ValidId() {
        assertDoesNotThrow(() -> {
            orderItemService.delete(1);
        });

        verify(orderItemRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).deleteById(1);
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderItemService.delete(100);
        });

        verify(orderItemRepository, times(1)).findById(100);
        verify(orderItemRepository, times(0)).deleteById(100);
    }

    @Test
    public void updateStatus_ValidId_ExistingOrderItem() throws Exception {
        OrderItem orderItemForUpdate = new OrderItem(2, "bez bibera", OrderItemStatus.PREPARATION,
                2, new Order(), new MenuItem(), null, new Cook());
        orderItemForUpdate.setId(1);

        when(orderItemRepository.save(any())).thenAnswer(a -> a.getArgument(0));

        OrderItem updatedOrderItem = orderItemService.updateStatus(orderItemForUpdate.getId(), "Pripremljeno");
        assertEquals(OrderItemStatus.PREPARED, updatedOrderItem.getStatus());
    }


    @Test
    public void updateStatus_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> {
            orderItemService.updateStatus(null, "Pripremljeno");
        });
    }

    @Test
    public void updateStatus_InvalidStatus_ThrowsBadLogicException() {
        assertThrows(BadLogicException.class, () -> {
            orderItemService.updateStatus(1, " ");
        });
    }

}
