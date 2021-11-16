package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.order.CreateOrderDto;
import com.kti.restaurant.dto.order.OrderDto;
import com.kti.restaurant.dto.order.UpdateOrderDto;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.service.contract.IRestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    private IRestaurantTableService restaurantTableService;

    @Autowired
    public OrderMapper(IRestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    public Order fromCreateOrderDtoToOrder(CreateOrderDto createOrderDto) throws Exception {
        return new Order(createOrderDto.getStatus(), createOrderDto.getDateOfOrder(), createOrderDto.getPrice(),
                findRestaurantTableById(createOrderDto.getTableId()));
    }
    public OrderDto fromOrderToOrderDto(Order order) {
        return new OrderDto(order.getStatus(), order.getDateOfOrder(),
                order.getPrice(),order.getTable().getId());
    }

    public Order fromUpdateOrderDtoToOrder(UpdateOrderDto orderDto) {
        return new Order(orderDto.getStatus(),orderDto.getDateOfOrder(),orderDto.getPrice());
    }

    private RestaurantTable findRestaurantTableById(Integer id) throws Exception {
        return restaurantTableService.findById(id);
    }
}
