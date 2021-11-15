package com.kti.restaurant.service.implementation;

import com.kti.restaurant.model.Order;
import com.kti.restaurant.repository.OrderRepository;
import com.kti.restaurant.service.contract.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderService implements IOrderService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAll() { return orderRepository.findAll();}

    @Override
    public Order findById(Integer id) throws Exception {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Order create(Order order) throws Exception {
        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order) throws Exception {
        Order orderToUpdate = orderRepository.findById(order.getId()).get();
        if (orderToUpdate == null) {
            throw new Exception("Entity with given id does not exist in the system.");
        }
        // videti da li treba sto i konobar, mada mislim da ne
        orderToUpdate.setStatus(order.getStatus());
        orderToUpdate.setDateOfOrder(order.getDateOfOrder());
        orderToUpdate.setPrice(order.getPrice());
        orderRepository.save(orderToUpdate);
        return orderToUpdate;
    }

    @Override
    public void delete(Integer id) throws Exception {
        orderRepository.deleteById(id);
    }
}
