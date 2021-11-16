package com.kti.restaurant.controller;

import com.kti.restaurant.dto.order.CreateOrderDto;
import com.kti.restaurant.dto.order.OrderDto;
import com.kti.restaurant.dto.order.UpdateOrderDto;
import com.kti.restaurant.mapper.OrderMapper;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.service.contract.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
        return new ResponseEntity<>(orderMapper.fromOrderToOrderDto(order),HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<?> getOrders(){
        List<OrderDto> orders = orderService.findAll().stream()
                .map(order->this.orderMapper.fromOrderToOrderDto(order)).collect(Collectors.toList());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id") Integer id) throws Exception {
        Order order= orderService.findById(id);
        return new ResponseEntity<>(orderMapper.fromOrderToOrderDto(order), HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Order> deleteOrder(@PathVariable("id") Integer id) throws Exception {
        orderService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid  @RequestBody UpdateOrderDto updateOrderDto,
                                             @PathVariable Integer id) throws Exception {
            return new ResponseEntity<>(orderMapper.fromOrderToOrderDto(orderService.update(orderMapper.fromUpdateOrderDtoToOrder(updateOrderDto),
                   id)),
                    HttpStatus.OK);

    }

}
