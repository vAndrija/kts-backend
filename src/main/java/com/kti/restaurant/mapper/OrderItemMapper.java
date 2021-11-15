package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.orderitem.CreateOrderItemDto;
import com.kti.restaurant.dto.orderitem.UpdateOrderItemDto;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.service.contract.IMenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    private IMenuItemService menuItemService;

    @Autowired
    public OrderItemMapper(IMenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    public OrderItem fromCreateOrderItemDtoToOrderItem(CreateOrderItemDto orderItemDto) throws Exception {
        //ubaciti i order kad se napravi
        return new OrderItem(orderItemDto.getQuantity(),orderItemDto.getNote(), orderItemDto.getStatus(),
                orderItemDto.getPriority(), menuItemService.findById(orderItemDto.getMenuItem()));
    }

    //ubaciti bartender i cook
    public OrderItem fromUpdateOrderItemDtoToOrderItem(UpdateOrderItemDto orderItemDto) {
        return new OrderItem(orderItemDto.getId(), orderItemDto.getQuantity(), orderItemDto.getNote(),
                orderItemDto.getStatus(), orderItemDto.getPriority());
    }
}