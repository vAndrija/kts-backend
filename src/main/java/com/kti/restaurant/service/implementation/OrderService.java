package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.enums.OrderStatus;
import com.kti.restaurant.repository.OrderRepository;
import com.kti.restaurant.service.contract.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            throw new MissingEntityException("Order with given id does not exist in the system.");
        }
        return order;
    }

    @Override
    public Order create(Order order) throws Exception {
        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order, Integer id) throws Exception {
        Order orderToUpdate = this.findById(id);
        // videti da li treba sto i konobar, mada mislim da ne
        orderToUpdate.setStatus(order.getStatus());
        orderToUpdate.setDateOfOrder(order.getDateOfOrder());
        orderToUpdate.setPrice(order.getPrice());
        orderRepository.save(orderToUpdate);
        return orderToUpdate;
    }

    @Override
    public void delete(Integer id) throws Exception {
        this.findById(id);
        orderRepository.deleteById(id);
    }

    @Override
    public Collection<Order> filterByStatus(String status) {
        Collection<Order> filteredOrders = orderRepository.findOrderByStatus(OrderStatus.valueOf(status));
        if(filteredOrders == null){
            throw new IllegalArgumentException("Given order status does not exist in the system.");
        }
        return filteredOrders;
    }
}
