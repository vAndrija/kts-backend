package com.kti.restaurant.controller;

import com.kti.restaurant.dto.orderitem.CreateOrderItemDto;
import com.kti.restaurant.dto.orderitem.OrderItemDto;
import com.kti.restaurant.dto.orderitem.UpdateOrderItemDto;
import com.kti.restaurant.mapper.OrderItemMapper;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.service.UserService;
import com.kti.restaurant.service.contract.IOrderItemService;
import com.kti.restaurant.service.contract.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/order-items")
public class OrderItemController {

    private IOrderItemService orderItemService;
    private OrderItemMapper orderItemMapper;
    private IOrderService orderService;
    private UserService userService;

    @Autowired
    public OrderItemController(IOrderItemService orderItemService, IOrderService orderService, UserService userService, OrderItemMapper orderItemMapper) {
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.userService = userService;
        this.orderItemMapper = orderItemMapper;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('WAITER')")
    public ResponseEntity<?> createOrderItem(@Valid @RequestBody CreateOrderItemDto newOrderItem) throws Exception {
        OrderItem orderItem = orderItemService.create(orderItemMapper.fromCreateOrderItemDtoToOrderItem(newOrderItem));
        return new ResponseEntity<>(orderItemMapper.fromOrderItemToOrderItemDto(orderItem), HttpStatus.CREATED);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER', 'MANAGER')")
    public ResponseEntity<?> getOrderItems() {
        List<OrderItemDto> orderItems = orderItemService.findAll().stream()
                .map(orderItem -> this.orderItemMapper.fromOrderItemToOrderItemDto(orderItem)).collect(Collectors.toList());
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER', 'MANAGER')")
    public ResponseEntity<?> getOrderItem(@PathVariable("id") Integer id) throws Exception {
        OrderItem orderItem = orderItemService.findById(id);
        return new ResponseEntity<>(orderItemMapper.fromOrderItemToOrderItemDto(orderItem), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER', 'MANAGER')")
    public ResponseEntity<OrderItem> deleteOrderItem(@PathVariable("id") Integer id) throws Exception {
        orderItemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER')")
    public ResponseEntity<?> updateOrderItem(@Valid @RequestBody UpdateOrderItemDto updateOrderItemDto,
                                             @PathVariable Integer id) throws Exception {
    	return new ResponseEntity<>(
                orderItemMapper.fromOrderItemToOrderItemDto(orderItemService.
                        update(orderItemMapper.fromUpdateOrderItemDtoToOrderItem(updateOrderItemDto), id)),
                HttpStatus.OK);
    }

    @GetMapping("/employee/{id}")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER', 'MANAGER')")
    public ResponseEntity<?> getOrderItemsForEmployee(@RequestParam Integer page, @RequestParam Integer size, @PathVariable("id") Integer id) throws Exception {

        if (userService.findById(id).getRoles().get(0).getName().equals("ROLE_WAITER")) {
            Page<Order> orders = orderService.findByWaiter(id, Pageable.unpaged());
            List<OrderItem> orderItems = orderItemService.findByOrdersAndWaiter(orders.getContent());
            int start = (int) PageRequest.of(page, size).getOffset();
            int end = Math.min((start + PageRequest.of(page, size).getPageSize()), orderItems.size());
            Page<OrderItemDto> orderItemDtoPage = new PageImpl<>(orderItems.stream()
                    .map(orderItem -> this.orderItemMapper.fromOrderItemToOrderItemDto(orderItem)).collect(Collectors.toList()).subList(start, end),
                    PageRequest.of(page, size), orderItems.size());
            return new ResponseEntity<>(orderItemDtoPage, HttpStatus.OK);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderItemDto> orderItems = orderItemService.findByEmployee(pageable, id)
                .map(orderItem -> this.orderItemMapper.fromOrderItemToOrderItemDto(orderItem));
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping(value = "/unaccepted")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER', 'MANAGER')")
    public ResponseEntity<?> getUnacceptedOrderItems(@RequestParam Integer page, @RequestParam Integer size) throws Exception {
    	Pageable pageable = PageRequest.of(page, size);
    	Page<OrderItemDto> orderItems = orderItemService.findUnacceptedOrderItems(pageable)
                .map(orderItem -> this.orderItemMapper.fromOrderItemToOrderItemDto(orderItem));
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }
    
    @PostMapping("/status/{id}")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER')")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Integer id, @RequestBody String status) throws Exception {
        OrderItem orderItem = orderItemService.updateStatus(id, status);
        return new ResponseEntity<>(orderItemMapper.fromOrderItemToOrderItemDto(orderItem), HttpStatus.OK);
    }

    @GetMapping("/order/{id}")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER', 'MANAGER')")
    public ResponseEntity<?> getOrderItemsForOrder(@PathVariable("id") Integer id) throws Exception {
        List<OrderItemDto> orderItems = orderItemService.findByOrder(id).stream()
                .map(orderItem -> this.orderItemMapper.fromOrderItemToOrderItemDto(orderItem)).collect(Collectors.toList());
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping(value = "/filter/{id}/{status}")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER', 'MANAGER')")
    public ResponseEntity<?> getFilteredOrderItemsByStatus(@PathVariable("id") Integer id,
                                                           @PathVariable("status") String status,
                                                           @RequestParam Integer page, @RequestParam Integer size)
            throws Exception {
        if (userService.findById(id).getRoles().get(0).getName().equals("ROLE_WAITER")) {
            Page<Order> orders = orderService.findByWaiter(id, Pageable.unpaged());
            List<OrderItem> orderItems = orderItemService.findByOrdersAndStatus(orders.getContent(), status);
            int start = (int) PageRequest.of(page, size).getOffset();
            int end = Math.min((start + PageRequest.of(page, size).getPageSize()), orderItems.size());
            Page<OrderItemDto> orderItemDtoPage = new PageImpl<>(orderItems.stream()
                    .map(orderItem -> this.orderItemMapper.fromOrderItemToOrderItemDto(orderItem)).collect(Collectors.toList()).subList(start, end),
                    PageRequest.of(page, size), orderItems.size());
            return new ResponseEntity<>(orderItemDtoPage, HttpStatus.OK);
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<OrderItemDto> orderItems = orderItemService.findByEmployeeAndStatus(id, status, pageable)
                .map(orderItem -> this.orderItemMapper.fromOrderItemToOrderItemDto(orderItem));
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

}
