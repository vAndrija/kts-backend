package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.orderitem.CreateOrderItemDto;
import com.kti.restaurant.dto.orderitem.OrderItemDto;
import com.kti.restaurant.dto.orderitem.UpdateOrderItemDto;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.service.contract.IMenuItemService;
import com.kti.restaurant.service.contract.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    private IMenuItemService menuItemService;
    private IOrderService orderService;
    @Autowired
    public OrderItemMapper(IMenuItemService menuItemService, IOrderService orderService) {
        this.menuItemService = menuItemService;
        this.orderService = orderService;
    }

    public OrderItem fromCreateOrderItemDtoToOrderItem(CreateOrderItemDto orderItemDto) throws Exception {
        return new OrderItem(orderItemDto.getQuantity(),orderItemDto.getNote(), orderItemDto.getStatus(),
                orderItemDto.getPriority(), findMenuItemById(orderItemDto.getMenuItem()), findOrderById(orderItemDto.getOrder()));
    }

    //ubaciti bartender i cook
    public OrderItem fromUpdateOrderItemDtoToOrderItem(UpdateOrderItemDto orderItemDto) {
        return new OrderItem(orderItemDto.getId(), orderItemDto.getQuantity(), orderItemDto.getNote(),
                orderItemDto.getStatus(), orderItemDto.getPriority());
    }

    public OrderItemDto fromOrderItemToOrderItemDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getId(), orderItem.getQuantity(), orderItem.getNote(),
                orderItem.getStatus(), orderItem.getPriority(), orderItem.getOrder().getId());
    }

    private MenuItem findMenuItemById(Integer id) throws Exception {
        MenuItem menuItem = menuItemService.findById(id);

        if(menuItem == null){
            throw new MissingEntityException("The menu item with given id does not exist in the system.");
        }

        return menuItem;
    }

    private Order findOrderById(Integer id) throws Exception {
        Order order = orderService.findById(id);

        if(order == null){
            throw new MissingEntityException("The order with given id does not exist in the system.");
        }

        return order;
    }
}