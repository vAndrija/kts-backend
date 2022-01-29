package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.model.enums.OrderItemStatus;
import com.kti.restaurant.repository.OrderItemRepository;
import com.kti.restaurant.service.UserService;
import com.kti.restaurant.service.contract.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderItemService implements IOrderItemService {
    private OrderItemRepository orderItemRepository;
    private UserService userService;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, UserService userService) {
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
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
        if (orderItemToUpdate.getStatus().equals(orderItem.getStatus()) &&
                (!orderItemToUpdate.getStatus().equals(OrderItemStatus.ORDERED))) {
            throw new BadLogicException("Order item cannot be changed.");
        }

        orderItemToUpdate.setStatus(orderItem.getStatus());
        orderItemToUpdate.setQuantity(orderItem.getQuantity());
        orderItemToUpdate.setPriority(orderItem.getPriority());
        orderItemToUpdate.setNote(orderItem.getNote());
        orderItemToUpdate.setMenuItem(orderItem.getMenuItem());
        if (orderItem.getCook() != null) {
            orderItemToUpdate.setCook(orderItem.getCook());
        } else if (orderItem.getBartender() != null) {
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
    public List<OrderItem> findOrderItemsInPeriodForMenuItem(LocalDateTime startDate, LocalDateTime endDate, Integer menuItemId) {
        return orderItemRepository.findSalesForMenuItem(startDate, endDate, menuItemId);
    }

    @Override
    public List<OrderItem> findByCook(Integer cookId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderItemRepository.findByCookForDate(cookId, startDate, endDate);
    }

    @Override
    public List<OrderItem> findByBartender(Integer bartenderId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderItemRepository.findByBartenderForDate(bartenderId, startDate, endDate);
    }

    @Override
    public Page<OrderItem> findByEmployee(Pageable pageable, Integer employeeId) {
        if (userService.findById(employeeId) == null) {
            throw new MissingEntityException("Bartender/cook with given id does not exist in the system.");
        }
        return orderItemRepository.findByEmployee(pageable, employeeId);
    }
    
    @Override
    public Page<OrderItem> findUnacceptedOrderItems(Pageable pageable) {
    	return orderItemRepository.findUnacceptedOrderItems(pageable);
    }

    @Override
    public OrderItem updateStatus(Integer id, String status) throws Exception {
        OrderItem orderItemToUpdate = this.findById(id);

        if (Objects.equals(status, " ")) {
            throw new BadLogicException("Given status cannot be empty.");
        }
        orderItemToUpdate.setStatus(OrderItemStatus.findType(status));
        orderItemRepository.save(orderItemToUpdate);
        return orderItemToUpdate;
    }

    @Override
    public OrderItem findByIdWithOrderAndWaiter(Integer orderItemId) {
        return orderItemRepository.findByIdWithOrderAndWaiter(orderItemId);
    }

    @Override
    public List<OrderItem> findByOrder(Integer id) {
        return orderItemRepository.findByOrder(id);
    }

    @Override
    public boolean checkIfServed(Integer id) {
        List<OrderItem> orderItems = this.findByOrder(id);
        for (OrderItem o : orderItems) {
            if (o.getStatus().getType().equals("Poručeno") || o.getStatus().getType().equals("U pripremi") ||
                    o.getStatus().getType().equals("Pripremljeno")) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Page<OrderItem> findByEmployeeAndStatus(Integer id, String status, Pageable pageable) {
        if (userService.findById(id) == null) {
            throw new MissingEntityException("Bartender/cook with given id does not exist in the system.");
        }
        if (status.equals(" ")) {
            throw new BadLogicException("Given order item status does not exist in the system.");
        }
        return orderItemRepository.findByEmployeeAndStatus(id, OrderItemStatus.findType(status), pageable);
    }

    @Override
    public List<OrderItem> findByOrdersAndWaiter(List<Order> orders) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (Order o : orders) {
            List<OrderItem> oi = orderItemRepository.findByOrderForWaiter(o.getId());
            orderItems.addAll(oi);
        }
        return orderItems;
    }

    @Override
    public List<OrderItem> findByOrdersAndStatus(List<Order> orders, String status) {
        if (status.equals(" ")) {
            throw new BadLogicException("Given order item status does not exist in the system.");
        }
        List<OrderItem> orderItems = new ArrayList<>();
        for (Order o : orders) {
            List<OrderItem> oi = orderItemRepository.findByOrderAndStatus(o.getId(), OrderItemStatus.findType(status));
            orderItems.addAll(oi);
        }
        return orderItems;
    }
}
