package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Bartender;
import com.kti.restaurant.model.Cook;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.model.enums.OrderItemStatus;
import com.kti.restaurant.repository.OrderItemRepository;
import com.kti.restaurant.service.contract.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class OrderItemService implements IOrderItemService {
    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    @Override
    public OrderItem findById(Integer id) throws Exception {
        OrderItem orderItem = orderItemRepository.findById(id).orElse(null);

        if (orderItem == null) {
            throw new MissingEntityException("Order item with given id does not exist in the system.");
        }

        return orderItem;

    }

    @Override
    public OrderItem create(OrderItem orderItem) throws Exception {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem update(OrderItem orderItem, Integer id) throws Exception {
        OrderItem orderItemToUpdate = this.findById(id);
        if(!orderItemToUpdate.getStatus().equals(OrderItemStatus.ORDERED) ||
                (orderItemToUpdate.getStatus().equals(orderItem.getStatus())) && !orderItemToUpdate.getStatus().equals(OrderItemStatus.ORDERED)){
            throw new BadLogicException("Order item cannot be changed.");
        }
        orderItemToUpdate.setStatus(orderItem.getStatus());
        orderItemToUpdate.setQuantity(orderItem.getQuantity());
        orderItemToUpdate.setPriority(orderItem.getPriority());
        orderItemToUpdate.setNote(orderItem.getNote());
        orderItemToUpdate.setMenuItem(orderItem.getMenuItem());
        if(orderItem.getCook() != null){
            orderItemToUpdate.setCook(orderItem.getCook());
        }else if(orderItem.getBartender() != null){
            orderItemToUpdate.setBartender(orderItem.getBartender());
        }
        orderItemRepository.save(orderItemToUpdate);

        return orderItemToUpdate;
    }

    @Override
    public void delete(Integer id) throws Exception {
        this.findById(id);
        orderItemRepository.deleteById(id);
    }

    @Override
    public List<OrderItem> findOrderItemsInPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return orderItemRepository.findOrderItemsByDate(startDate, endDate);
    }

    @Override
    public List<OrderItem> findByCook(Cook cook, LocalDateTime startDate, LocalDateTime endDate) {
        return orderItemRepository.findByCookForDate(cook, startDate, endDate);
    }

    @Override
    public List<OrderItem> findByBartender(Bartender bartender, LocalDateTime startDate, LocalDateTime endDate) {
        return orderItemRepository.findByBartenderForDate(bartender, startDate, endDate);
    }
}
