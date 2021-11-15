package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.order.CreateOrderDto;
import com.kti.restaurant.dto.order.UpdateOrderDto;
import com.kti.restaurant.exception.MissingEntityException;
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
                findRestaurantTableById(createOrderDto.getTable()));
    }

    public Order fromUpdateOrderDtoToOrder(UpdateOrderDto orderDto) {
        return new Order(orderDto.getId(), orderDto.getStatus(),orderDto.getDateOfOrder(),orderDto.getPrice());
    }

    private RestaurantTable findRestaurantTableById(Integer id) throws Exception {
        RestaurantTable restaurantTable = restaurantTableService.findById(id);

        if(restaurantTable == null){
            throw new MissingEntityException("The restaurant table with given id does not exist in the system.");
        }

        return restaurantTable;
    }
}
