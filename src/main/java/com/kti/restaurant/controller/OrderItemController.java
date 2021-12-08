package com.kti.restaurant.controller;

import com.kti.restaurant.dto.orderitem.CreateOrderItemDto;
import com.kti.restaurant.dto.orderitem.OrderItemDto;
import com.kti.restaurant.dto.orderitem.UpdateOrderItemDto;
import com.kti.restaurant.mapper.OrderItemMapper;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.service.contract.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value= "/api/v1/order-items")
public class OrderItemController {

    private IOrderItemService orderItemService;
    private OrderItemMapper orderItemMapper;

    @Autowired
    public OrderItemController(IOrderItemService orderItemService, OrderItemMapper orderItemMapper) {
        this.orderItemService = orderItemService;
        this.orderItemMapper = orderItemMapper;
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('WAITER')")
    public ResponseEntity<?> createOrderItem(@Valid @RequestBody CreateOrderItemDto newOrderItem) throws Exception {
      OrderItem orderItem = orderItemService.create(orderItemMapper.fromCreateOrderItemDtoToOrderItem(newOrderItem));
      if(orderItem == null){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(orderItemMapper.fromOrderItemToOrderItemDto(orderItem),HttpStatus.CREATED);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER', 'MANAGER')")
    public ResponseEntity<?> getOrderItems() {
        List<OrderItemDto> orderItems = orderItemService.findAll().stream()
                .map(orderItem->this.orderItemMapper.fromOrderItemToOrderItemDto(orderItem)).collect(Collectors.toList());
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER', 'MANAGER')")
    public ResponseEntity<?> getOrderItem(@PathVariable("id") Integer id) throws Exception {
        OrderItem orderItem = orderItemService.findById(id);
        return new ResponseEntity<>( orderItemMapper.fromOrderItemToOrderItemDto(orderItem), HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
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
}
