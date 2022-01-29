package com.kti.restaurant.repository;

import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.enums.OrderStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    @ParameterizedTest
    @MethodSource("provideIdAndStatusForFindByWaiterAndStatus")
    public void findOrdersByWaiterAndStatus_ReturnsOrder(Integer id, OrderStatus status, Integer expected) {
        Pageable pageable = PageRequest.of(0, 8);
        Page<Order> orders = orderRepository.findByWaiterAndStatus(id, status, pageable);
        assertEquals(expected, orders.getContent().size());
    }

    private static Stream<Arguments> provideIdAndStatusForFindByWaiterAndStatus() {
        return Stream.of(
                Arguments.of(7, OrderStatus.ORDERED, 2),
                Arguments.of(70, OrderStatus.ORDERED, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideIdForFindByWaiter")
    public void findOrdersByWaiter_ReturnsOrderItems(Integer id, int expected) {
        Pageable pageable = PageRequest.of(0, 8);
        Page<Order> orders = orderRepository.findByWaiter(id, pageable);
        assertEquals(expected, orders.getContent().size());
    }

    private static Stream<Arguments> provideIdForFindByWaiter() {
        return Stream.of(
                Arguments.of(8, 1),
                Arguments.of(10, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideIdForFindByRestaurantTable")
    public void findOrdersByRestaurantTable_ReturnsOrders(Integer id, int expected) {
        List<Order> orders = orderRepository.findByRestaurantTable(id);
        assertEquals(expected, orders.size());
    }

    private static Stream<Arguments> provideIdForFindByRestaurantTable() {
        return Stream.of(
                Arguments.of(3, 1),
                Arguments.of(11, 0)
        );
    }
}
