package com.kti.restaurant.service.implementation;

import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.repository.OrderItemRepository;
import com.kti.restaurant.service.contract.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService implements IOrderItemService {
    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderItem> findAll() { return orderItemRepository.findAll();}

    @Override
    public OrderItem findById(Integer id) { return orderItemRepository.findById(id).orElseGet(null);}

    @Override
    public OrderItem create(OrderItem orderItem) { return orderItemRepository.save(orderItem); }

    @Override
    public OrderItem update(OrderItem orderItem) throws Exception {
        OrderItem orderItemToUpdate = orderItemRepository.findById(orderItem.getId()).get();
        if(orderItemToUpdate == null){
            throw new Exception("Entity with given id does not exist in the system.");
        }
        orderItemToUpdate.setStatus(orderItem.getStatus());
        orderItemToUpdate.setQuantity(orderItem.getQuantity());
        orderItemToUpdate.setPriority(orderItem.getPriority());
        orderItemToUpdate.setNote(orderItem.getNote());
        //treba dodati i kuvara i sankera, mislim da menuitem ne treba
        orderItemRepository.save(orderItemToUpdate);
        return orderItemToUpdate;
    }

    @Override
    public void delete(Integer id) {orderItemRepository.deleteById(id);}
}
