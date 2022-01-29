package com.kti.restaurant.repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import com.kti.restaurant.model.enums.OrderItemStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import com.kti.restaurant.model.OrderItem;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class OrderItemRepositoryTests {

    @Autowired
    private OrderItemRepository orderItemRepository;


    @ParameterizedTest
    @MethodSource("provideDatesForFindOrderItemsByDate")
    public void findOrderItemsByDate_ReturnsOrderItems(LocalDateTime startDate, LocalDateTime endDate, int expected) {
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByDate(startDate, endDate);
        assertEquals(expected, orderItems.size());
    }

    private static Stream<Arguments> provideDatesForFindOrderItemsByDate() {
        return Stream.of(
                Arguments.of(LocalDateTime.parse("2022-11-18T08:00"), LocalDateTime.parse("2022-11-19T08:00"), 2),
                Arguments.of(LocalDateTime.parse("2022-11-18T14:00"), LocalDateTime.parse("2021-01-01T14:00"), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDatesAndMenuItemForFindSalesForMenuItem")
    public void findSalesForMenuItem_ReturnsOrderItems(LocalDateTime startDate, LocalDateTime endDate, Integer menuId, int expected) {
        List<OrderItem> orderItems = orderItemRepository.findSalesForMenuItem(startDate, endDate, menuId);
        assertEquals(expected, orderItems.size());
    }

    private static Stream<Arguments> provideDatesAndMenuItemForFindSalesForMenuItem() {
        return Stream.of(
                Arguments.of(LocalDateTime.parse("2021-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"), 1, 2),
                Arguments.of(LocalDateTime.parse("2022-11-18T08:00"), LocalDateTime.parse("2022-11-01T23:00"), 1, 0),
                Arguments.of(LocalDateTime.parse("2021-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"), 1000, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDateAndCookIdForFindByCookForDate")
    public void findByCookForDate_ReturnOrderItems(LocalDateTime startDate, LocalDateTime endDate, Integer cookId, int expected) {
        List<OrderItem> orderItems = orderItemRepository.findByCookForDate(cookId, startDate, endDate);
        assertEquals(expected, orderItems.size());
    }

    private static Stream<Arguments> provideDateAndCookIdForFindByCookForDate() {
        return Stream.of(
                Arguments.of(LocalDateTime.parse("2021-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"), 4, 2),
                Arguments.of(LocalDateTime.parse("2025-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"), 4, 0),
                Arguments.of(LocalDateTime.parse("2021-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"), 1000, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDateAndBartenderIdForFindByBartenderForDate")
    public void findByBartenderForDate_ReturnsOrderItems(LocalDateTime startDate, LocalDateTime endDate, Integer bartenderId, int expected) {
        List<OrderItem> orderItems = orderItemRepository.findByBartenderForDate(bartenderId, startDate, endDate);
        assertEquals(expected, orderItems.size());
    }

    private static Stream<Arguments> provideDateAndBartenderIdForFindByBartenderForDate() {
        return Stream.of(
                Arguments.of(LocalDateTime.parse("2022-11-18T08:00"), LocalDateTime.parse("2022-11-28T23:00"), 2, 1),
                Arguments.of(LocalDateTime.parse("2022-12-22T08:00"), LocalDateTime.parse("2022-11-01T11:45"), 2, 0),
                Arguments.of(LocalDateTime.parse("2022-11-18T08:00"), LocalDateTime.parse("2022-11-28T23:00"), 200, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCookIdBartenderIdForFindByEmployee")
    public void findByEmployee_ReturnsOrderItems(Integer id, int expected) {
        Page<OrderItem> orderItems = orderItemRepository.findByEmployee(PageRequest.of(0, 5), id);
        List<OrderItem> orderItemList = orderItems.getContent();
        assertEquals(expected, orderItemList.size());
    }

    private static Stream<Arguments> provideCookIdBartenderIdForFindByEmployee() {
        return Stream.of(
                Arguments.of(3, 2),
                Arguments.of(2, 2),
                Arguments.of(1, 0)
        );
    }


    @ParameterizedTest
    @MethodSource("provideEmployeeIdForFindByEmployee")
    public void findOrderItemsByEmployee_OrderItems(Integer id, int expected) {
        Pageable pageable = PageRequest.of(0, 8);
        Page<OrderItem> orderItemPage = orderItemRepository.findByEmployee(pageable, id);
        assertEquals(expected, orderItemPage.getContent().size());
    }

    private static Stream<Arguments> provideEmployeeIdForFindByEmployee() {
        return Stream.of(
                Arguments.of(2, 2),
                Arguments.of(4, 2),
                Arguments.of(-1, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOrderIdForFindByOrder")
    public void findOrderItemsByOrder_ReturnsOrderItems(Integer id, int expected) {
        List<OrderItem> orderItems = orderItemRepository.findByOrder(id);
        assertEquals(expected, orderItems.size());
    }

    private static Stream<Arguments> provideOrderIdForFindByOrder() {
        return Stream.of(
                Arguments.of(1, 9),
                Arguments.of(2, 2),
                Arguments.of(-1, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideEmployeeIdAndStatusForFindByEmployeeAndStatus")
    public void findOrderItemsByEmployeeAndStatus_ReturnsOrderItems(Integer id, OrderItemStatus status, int expected) {
        Pageable pageable = PageRequest.of(0, 8);
        Page<OrderItem> orderItems = orderItemRepository.findByEmployeeAndStatus(id, status, pageable);
        assertEquals(expected, orderItems.getContent().size());
    }

    private static Stream<Arguments> provideEmployeeIdAndStatusForFindByEmployeeAndStatus() {
        return Stream.of(
                Arguments.of(4, OrderItemStatus.PREPARATION, 2),
                Arguments.of(3, OrderItemStatus.PREPARED, 1),
                Arguments.of(4, OrderItemStatus.SERVED, 0)
        );
    }
    
    @ParameterizedTest
    @MethodSource("provideExpectedUnacceptedOrderItems")
    public void findUnacceptedOrderItems_ReturnsOrderItems(int expected) {
        Pageable pageable = PageRequest.of(0, 8);
        Page<OrderItem> orderItems = orderItemRepository.findUnacceptedOrderItems(pageable);
        assertEquals(expected, orderItems.getContent().size());
    }

    private static Stream<Arguments> provideExpectedUnacceptedOrderItems() {
        return Stream.of(
                Arguments.of(3)
        );
    }


    @ParameterizedTest
    @MethodSource("provideOrderIdForFindByOrderAndWaiter")
    public void findOrderItemsByOrderAndWaiter_ReturnsOrderItems(Integer id, int expected) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderForWaiter(id);
        assertEquals(expected, orderItems.size());
    }

    private static Stream<Arguments> provideOrderIdForFindByOrderAndWaiter() {
        return Stream.of(
                Arguments.of(1, 2),
                Arguments.of(2, 2),
                Arguments.of(5, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOrderIdAndStatusForFindByOrderAndStatus")
    public void findOrderItemsByOrderAndStatus_ReturnsOrderItems(Integer id, OrderItemStatus status, int expected) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderAndStatus(id, status);
        assertEquals(expected, orderItems.size());
    }

    private static Stream<Arguments> provideOrderIdAndStatusForFindByOrderAndStatus() {
        return Stream.of(
                Arguments.of(1, OrderItemStatus.PREPARED , 2),
                Arguments.of(2, OrderItemStatus.SERVED, 2)
        );
    }

}
