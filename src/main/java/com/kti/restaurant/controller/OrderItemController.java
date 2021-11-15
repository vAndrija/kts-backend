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

import java.util.Collection;

@RestController
@CrossOrigin
@RequestMapping(value= "/api/order-items")
public class OrderItemController {

    private IOrderItemService orderItemService;
    private OrderItemMapper orderItemMapper;

    @Autowired
    public OrderItemController(IOrderItemService orderItemService, OrderItemMapper orderItemMapper) {
        this.orderItemService = orderItemService;
        this.orderItemMapper = orderItemMapper;
    }

    @PostMapping("")
    public ResponseEntity<?> createOrderItem(@RequestBody CreateOrderItemDto newOrderItem) throws Exception {
      OrderItem orderItem = orderItemService.create(orderItemMapper.fromCreateOrderItemDtoToOrderItem(newOrderItem));
      if(orderItem == null){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      return new ResponseEntity<>(orderItem,HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Collection<OrderItem>> getOrderItems(){
        Collection<OrderItem> orderItems = orderItemService.findAll();
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderItem> getOrderItem(@PathVariable("id") Integer id) {
        OrderItem orderItem = orderItemService.findById(id);
        if (orderItem == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(orderItem, HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<OrderItem> deleteOrderItem(@PathVariable("id") Integer id) {
        orderItemService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("")
    public ResponseEntity<OrderItem> updateOrderItem(@RequestBody UpdateOrderItemDto updateOrderItemDto) {
        try {
            return new ResponseEntity<>(orderItemService.update(orderItemMapper.fromUpdateOrderItemDtoToOrderItem(updateOrderItemDto)),
                    HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
