package com.kti.restaurant.mapper;

import com.kti.restaurant.dto.orderitem.CreateOrderItemDto;
import com.kti.restaurant.dto.orderitem.NotificationOrderItemDto;
import com.kti.restaurant.dto.orderitem.OrderItemDto;
import com.kti.restaurant.dto.orderitem.UpdateOrderItemDto;
import com.kti.restaurant.model.*;
import com.kti.restaurant.service.contract.IBartenderService;
import com.kti.restaurant.service.contract.ICookService;
import com.kti.restaurant.service.contract.IMenuItemService;
import com.kti.restaurant.service.contract.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    private IMenuItemService menuItemService;
    private IOrderService orderService;
    private MenuItemMapper menuItemMapper;
    private IBartenderService bartenderService;
    private ICookService cookService;

    @Autowired
    public OrderItemMapper(IMenuItemService menuItemService, IOrderService orderService,
                           IBartenderService bartenderService, ICookService cookService, MenuItemMapper menuItemMapper) {
        this.menuItemService = menuItemService;
        this.orderService = orderService;
        this.menuItemMapper = menuItemMapper;
        this.bartenderService = bartenderService;
        this.cookService = cookService;
    }

    public OrderItem fromCreateOrderItemDtoToOrderItem(CreateOrderItemDto orderItemDto) throws Exception {
        return new OrderItem(orderItemDto.getQuantity(), orderItemDto.getNote(), orderItemDto.getStatus(),
                orderItemDto.getPriority(), findMenuItemById(orderItemDto.getMenuItemId()),
                findOrderById(orderItemDto.getOrderId()));
    }

    public OrderItem fromUpdateOrderItemDtoToOrderItem(UpdateOrderItemDto updateOrderItemDto) throws Exception {
        OrderItem orderItem = new OrderItem(updateOrderItemDto.getQuantity(), updateOrderItemDto.getNote(),
                updateOrderItemDto.getStatus(), updateOrderItemDto.getPriority(), findMenuItemById(updateOrderItemDto.getMenuItemId()),
                findOrderById(updateOrderItemDto.getOrderId()));
        if (updateOrderItemDto.getBartenderId() == null && updateOrderItemDto.getCookId() == null) {
            orderItem.setBartender(null);
            orderItem.setCook(null);
            return orderItem;
        } else if (updateOrderItemDto.getBartenderId() != null && updateOrderItemDto.getCookId() == null) {
            orderItem.setBartender(findBartenderById(updateOrderItemDto.getBartenderId()));
            orderItem.setCook(null);
            return orderItem;
        } else if (updateOrderItemDto.getBartenderId() == null && updateOrderItemDto.getCookId() != null) {
            orderItem.setBartender(null);
            orderItem.setCook(findCookById(updateOrderItemDto.getCookId()));
            return orderItem;
        }
        return null;

    }

    public OrderItemDto fromOrderItemToOrderItemDto(OrderItem orderItem) {

        OrderItemDto orderItemDto = new OrderItemDto(orderItem.getId(), orderItem.getQuantity(), orderItem.getNote(),
                orderItem.getStatus().getType(), orderItem.getPriority(), orderItem.getOrder().getId(),
                orderItem.getMenuItem().getId(), orderItem.getOrder().getDateOfOrder());
        if (orderItem.getBartender() == null && orderItem.getCook() == null) {
            orderItemDto.setBartenderId(null);
            orderItemDto.setCookId(null);
            return orderItemDto;
        } else if (orderItem.getBartender() != null && orderItem.getCook() == null) {
            orderItemDto.setBartenderId(orderItem.getBartender().getId());
            orderItemDto.setCookId(null);
            return orderItemDto;
        } else if (orderItem.getBartender() == null && orderItem.getCook() != null) {
            orderItemDto.setBartenderId(null);
            orderItemDto.setCookId(orderItem.getCook().getId());
            return orderItemDto;
        }
        return null;
    }

    public NotificationOrderItemDto fromOrderItemToNotificationOrderItemDto(OrderItem orderItem) {
        return new NotificationOrderItemDto(orderItem.getId(), orderItem.getNote(), orderItem.getQuantity(),
                orderItem.getStatus(), menuItemMapper.fromMenuItemToMenuItemDto(orderItem.getMenuItem()));
    }

    private MenuItem findMenuItemById(Integer id) throws Exception {
        return menuItemService.findById(id);
    }

    private Order findOrderById(Integer id) throws Exception {
        return orderService.findById(id);
    }

    private Bartender findBartenderById(Integer id) throws Exception {
        return bartenderService.findById(id);
    }

    private Cook findCookById(Integer id) throws Exception {
        return cookService.findById(id);
    }
}