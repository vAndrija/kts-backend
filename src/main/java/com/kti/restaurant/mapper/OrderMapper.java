package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.order.CreateOrderDto;
import com.kti.restaurant.dto.order.OrderDto;
import com.kti.restaurant.dto.order.UpdateOrderDto;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.model.Waiter;
import com.kti.restaurant.model.enums.OrderStatus;
import com.kti.restaurant.service.contract.IRestaurantTableService;
import com.kti.restaurant.service.contract.IWaiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    private IRestaurantTableService restaurantTableService;
    private IWaiterService waiterService;
    @Autowired
    public OrderMapper(IRestaurantTableService restaurantTableService, IWaiterService waiterService) {
        this.restaurantTableService = restaurantTableService;
        this.waiterService = waiterService;
    }

    public Order fromCreateOrderDtoToOrder(CreateOrderDto createOrderDto) throws Exception {
        return new Order(OrderStatus.findType(createOrderDto.getStatus()), createOrderDto.getDateOfOrder(), createOrderDto.getPrice(),
                findRestaurantTableById(createOrderDto.getTableId()),findWaiterById(createOrderDto.getWaiterId()));
    }
    public OrderDto fromOrderToOrderDto(Order order) {
        return new OrderDto(order.getId(), order.getStatus(), order.getDateOfOrder(),
                order.getPrice(),order.getTable().getId(), order.getWaiter().getId());
    }

    public Order fromUpdateOrderDtoToOrder(UpdateOrderDto orderDto) {
        return new Order(orderDto.getStatus(), orderDto.getPrice());
    }

    private RestaurantTable findRestaurantTableById(Integer id) throws Exception {
        return restaurantTableService.findById(id);
    }

    private Waiter findWaiterById(Integer id) throws Exception {
        return waiterService.findById(id);
    }
}
