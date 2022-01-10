package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Order;

import com.kti.restaurant.model.enums.OrderStatus;
import com.kti.restaurant.repository.OrderRepository;
import com.kti.restaurant.repository.UserRepository;
import com.kti.restaurant.service.UserService;
import com.kti.restaurant.service.contract.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class OrderService implements IOrderService {
    private OrderRepository orderRepository;
    private WaiterService waiterService;

    @Autowired
    public OrderService(OrderRepository orderRepository, WaiterService waiterService) {
        this.orderRepository = orderRepository;
        this.waiterService = waiterService;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

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
    public Page<Order> filterByStatus(Integer id, String status, Pageable pageable) throws Exception {
        if (waiterService.findById(id) == null) {
            throw new MissingEntityException("Waiter with given id does not exist in the system.");
        }
        if (status.equals(" ")) {
            throw new BadLogicException("Given order status does not exist in the system.");
        }
        return orderRepository.findByWaiterAndStatus(id, OrderStatus.findType(status), pageable);
    }

    @Override
    public Page<Order> findByWaiter(Integer id, Pageable pageable) throws Exception {
        if (waiterService.findById(id) == null) {
            throw new MissingEntityException("Waiter with given id does not exist in the system.");
        }
        return orderRepository.findByWaiter(id, pageable);
    }

    @Override
    public Order updateStatus(Integer id, String status) throws Exception {
        Order orderToUpdate = this.findById(id);
        if (Objects.equals(status, " ")) {
            throw new BadLogicException("Given status cannot be empty.");
        }
        orderToUpdate.setStatus(OrderStatus.findType(status));
        orderRepository.save(orderToUpdate);
        return orderToUpdate;
    }
}
