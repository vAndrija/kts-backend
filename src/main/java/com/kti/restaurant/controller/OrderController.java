package com.kti.restaurant.controller;

import com.kti.restaurant.dto.order.CreateOrderDto;
import com.kti.restaurant.dto.order.UpdateOrderDto;
import com.kti.restaurant.mapper.OrderMapper;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.service.contract.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@CrossOrigin
@RequestMapping(value= "/api/v1/orders")
public class OrderController {

    private IOrderService orderService;
    private OrderMapper orderMapper;


    @Autowired
    public OrderController(IOrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @PostMapping("")
    public ResponseEntity<?> createOrder(@Valid @RequestBody CreateOrderDto newOrder) throws Exception {
        Order order = orderService.create(orderMapper.fromCreateOrderDtoToOrder(newOrder));
        if(order == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(order,HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<Collection<Order>> getOrders(){
        Collection<Order> orders = orderService.findAll();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") Integer id) throws Exception {
        Order order= orderService.findById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Order> deleteOrder(@PathVariable("id") Integer id) throws Exception {
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("")
    public ResponseEntity<Order> updateOrder(@Valid  @RequestBody UpdateOrderDto updateOrderDto) throws Exception {
            return new ResponseEntity<>(orderService.update(orderMapper.fromUpdateOrderDtoToOrder(updateOrderDto),
                    updateOrderDto.getId()),
                    HttpStatus.OK);

    }

}
