package com.kti.restaurant.service.implementation;

import com.kti.restaurant.dto.tablereservation.TableReservationDto;
import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.*;
import com.kti.restaurant.model.enums.OrderItemStatus;
import com.kti.restaurant.model.enums.OrderStatus;
import com.kti.restaurant.repository.OrderItemRepository;
import com.kti.restaurant.service.UserService;
import com.kti.restaurant.utils.GsonUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    
    @Mock
    UserService userService;

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
        assertThrows(MissingEntityException.class, () -> orderItemService.findById(2));
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
        assertThrows(MissingEntityException.class, () -> orderItemService.update(null, 100));
    }

    @Test
    public void delete_ValidId() {
        assertDoesNotThrow(() -> orderItemService.delete(1));

        verify(orderItemRepository, times(1)).findById(1);
        verify(orderItemRepository, times(1)).deleteById(1);
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() {
        assertThrows(MissingEntityException.class, () -> orderItemService.delete(100));

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
        assertThrows(MissingEntityException.class, () -> orderItemService.updateStatus(null, "Pripremljeno"));
    }

    @Test
    public void updateStatus_InvalidStatus_ThrowsBadLogicException() {
        assertThrows(BadLogicException.class, () -> orderItemService.updateStatus(1, " "));
    }

    @Test
    public void findByOrdersAndWaiter_ValidOrderList_ReturnsOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        Order order = new Order();
        order.setId(1);

        OrderItem orderItem = new OrderItem(2, "bez bibera", OrderItemStatus.SERVED,
                2, order, new MenuItem(), null, new Cook());
        orderItem.setId(1);
        orders.add(order);
        orderItems.add(orderItem);

        when(orderItemRepository.findByOrderForWaiter(1)).thenReturn(orderItems);

        List<OrderItem> orderItemList = orderItemService.findByOrdersAndWaiter(orders);
        assertEquals(1, orderItemList.size());

    }
    @Test
    public void findByOrdersAndWaiter_EmptyOrderList_ReturnsEmptyList() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        OrderItem orderItem = new OrderItem(2, "bez bibera", OrderItemStatus.PREPARED,
                2, new Order(), new MenuItem(), null, new Cook());
        orderItem.setId(1);

        orderItems.add(orderItem);

        when(orderItemRepository.findByOrderForWaiter(1)).thenReturn(orderItems);

        List<OrderItem> orderItemList = orderItemService.findByOrdersAndWaiter(orders);
        assertEquals(0, orderItemList.size());

    }

    @Test
    public void findByOrdersAndStatus_ValidStatus_ReturnsOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();
        List<Order> orders = new ArrayList<>();

        Order order = new Order();
        order.setId(1);

        OrderItem orderItem = new OrderItem(2, "bez bibera", OrderItemStatus.PREPARED,
                2, order, new MenuItem(), null, new Cook());
        orderItem.setId(1);
        orders.add(order);
        orderItems.add(orderItem);

        when(orderItemRepository.findByOrderAndStatus(1, OrderItemStatus.PREPARED)).thenReturn(orderItems);

        List<OrderItem> orderItemList = orderItemService.findByOrdersAndStatus(orders, "Pripremljeno");
        assertEquals(1, orderItemList.size());

    }
    @Test
    public void findByOrdersAndStatus_InvalidStatus_ThrowsBadLogicException() {
        List<Order> orders = new ArrayList<>();
        assertThrows(BadLogicException.class, () -> orderItemService.findByOrdersAndStatus(orders, " "));

    }
    
    @Test
    public void checkIfServed_ValidStatus_ReturnsTrue() {
    	OrderItem orderItem = new OrderItem(2, "bez bibera", OrderItemStatus.SERVED,
                2, new Order(), new MenuItem(), null, new Cook());
        orderItem.setId(2);
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        orderItems.add(orderItem);
        
        when(orderItemRepository.findByOrder(2)).thenReturn(orderItems);
        assertTrue(orderItemService.checkIfServed(2));
    }
    
    @ParameterizedTest
    @MethodSource("provideInvalidStatusForOrderItem")
    public void checkIfServed_InvalidStatus_ReturnsFalse(OrderItemStatus status) {
    	OrderItem orderItem = new OrderItem(2, "bez bibera", status,
                2, new Order(), new MenuItem(), null, new Cook());
        orderItem.setId(2);
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        orderItems.add(orderItem);
        
        when(orderItemRepository.findByOrder(2)).thenReturn(orderItems);
        
        assertFalse(orderItemService.checkIfServed(2));
    }

    private static Stream<Arguments> provideInvalidStatusForOrderItem() {

        return Stream.of(
                Arguments.of(OrderItemStatus.ORDERED),
                Arguments.of(OrderItemStatus.PREPARATION),
                Arguments.of(OrderItemStatus.PREPARED)

        );
    }

    @Test
    public void FindByEmployeeAndStatus_InvalidUserId_ThrowsMissingEntityException() {
    	when(userService.findById(1)).thenReturn(null);
    	
    	assertThrows(MissingEntityException.class, () -> {
            orderItemService.findByEmployeeAndStatus(1, "status", null);
        });
    }
    
    @Test
    public void FindByEmployeeAndStatus_InvalidStatus_ThrowsBadLogicExcpetion() {
    	when(userService.findById(1)).thenReturn(new Bartender());
    	
    	assertThrows(BadLogicException.class, () -> {
            orderItemService.findByEmployeeAndStatus(1, " ", null);
        });
    	
    }
    //Vidjeti kako ovo na drugaciji nacin
    @Test
    public void FindByEmployeeAndStatus_ValidParameters_ReturnsPageableOrderItems() {
    	when(userService.findById(1)).thenReturn(new Bartender());
    	Pageable pageable = PageRequest.of(0,2);
    	
    	when(orderItemRepository.findByEmployeeAndStatus(1, OrderItemStatus.PREPARATION, pageable)).thenReturn(null);
    	assertNull(orderItemService.findByEmployeeAndStatus(1, "U pripemi", pageable));
    	
    }
}
