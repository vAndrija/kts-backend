package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.orderitem.CreateOrderItemDto;
import com.kti.restaurant.dto.orderitem.OrderItemDto;
import com.kti.restaurant.dto.orderitem.UpdateOrderItemDto;
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
                orderItemDto.getPriority(), findMenuItemById(orderItemDto.getMenuItemId()),
                findOrderById(orderItemDto.getOrderId()));
    }

    //ubaciti bartender i cook
    public OrderItem fromUpdateOrderItemDtoToOrderItem(UpdateOrderItemDto orderItemDto) throws Exception {
        return new OrderItem(orderItemDto.getQuantity(), orderItemDto.getNote(),
                orderItemDto.getStatus(), orderItemDto.getPriority(), findMenuItemById(orderItemDto.getMenuItemId()));
    }

    public OrderItemDto fromOrderItemToOrderItemDto(OrderItem orderItem) {
        return new OrderItemDto(orderItem.getQuantity(), orderItem.getNote(),
                orderItem.getStatus(), orderItem.getPriority(), orderItem.getOrder().getId());
    }

    private MenuItem findMenuItemById(Integer id) throws Exception {
        return menuItemService.findById(id);
    }

    private Order findOrderById(Integer id) throws Exception {
        return orderService.findById(id);
    }
}