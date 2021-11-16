package com.kti.restaurant.controller;

import com.kti.restaurant.dto.orderitem.CreateOrderItemDto;
import com.kti.restaurant.dto.orderitem.UpdateOrderItemDto;
import com.kti.restaurant.mapper.OrderItemMapper;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.service.contract.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

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
    public ResponseEntity<?> createOrderItem(@Valid @RequestBody CreateOrderItemDto newOrderItem) throws Exception {
      OrderItem orderItem = orderItemService.create(orderItemMapper.fromCreateOrderItemDtoToOrderItem(newOrderItem));
      if(orderItem == null){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(orderItemMapper.fromOrderItemToOrderItemDto(orderItem),HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Collection<OrderItem>> getOrderItems(){
        Collection<OrderItem> orderItems = orderItemService.findAll();
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable("id") Integer id) throws Exception {
        OrderItem orderItem = orderItemService.findById(id);
        return new ResponseEntity<>(orderItem, HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<OrderItem> deleteOrderItem(@PathVariable("id") Integer id) throws Exception {
        orderItemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(@Valid @RequestBody UpdateOrderItemDto updateOrderItemDto, @PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(orderItemService.update(orderItemMapper.fromUpdateOrderItemDtoToOrderItem(updateOrderItemDto), id),
                    HttpStatus.OK);
    }
}
