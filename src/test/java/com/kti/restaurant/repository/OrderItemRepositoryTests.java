package com.kti.restaurant.repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import com.kti.restaurant.model.OrderItem;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class OrderItemRepositoryTests {

    @Autowired
    private OrderItemRepository orderItemRepository;


    @ParameterizedTest
    @MethodSource("datesForFindOrderItemsByDate")
    public void findOrderItemsByDate(LocalDateTime startDate, LocalDateTime endDate, int expected) {
        List<OrderItem> orderItems = orderItemRepository.findOrderItemsByDate(startDate, endDate);
        assertEquals(expected, orderItems.size());
    }

    private static Stream<Arguments> datesForFindOrderItemsByDate() {
        return Stream.of(
                Arguments.of(LocalDateTime.parse("2022-11-18T08:00"), LocalDateTime.parse("2022-11-19T08:00"), 2),
                Arguments.of(LocalDateTime.parse("2022-11-18T14:00"), LocalDateTime.parse("2021-01-01T14:00"), 0)
        );
    }

    @ParameterizedTest
    @MethodSource("datesAndMenuItemForFindSalesForMenuItem")
    public void findSalesForMenuItem(LocalDateTime startDate, LocalDateTime endDate, int menuId, int expected) {
        List<OrderItem> orderItems = orderItemRepository.findSalesForMenuItem(startDate, endDate, menuId);
        assertEquals(expected, orderItems.size());
    }

    private static Stream<Arguments> datesAndMenuItemForFindSalesForMenuItem() {
        return Stream.of(
                Arguments.of(LocalDateTime.parse("2021-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"), 1, 2),
                Arguments.of(LocalDateTime.parse("2022-11-18T08:00"), LocalDateTime.parse("2022-11-01T23:00"), 1, 0),
                Arguments.of(LocalDateTime.parse("2021-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"), 1000, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("dateAndCookIdForFindByCookForDate")
    public void findByCookForDate(LocalDateTime startDate, LocalDateTime endDate, int cookId, int expected) {
        List<OrderItem> orderItems = orderItemRepository.findByCookForDate(cookId, startDate, endDate);
        assertEquals(expected, orderItems.size());
    }

    private static Stream<Arguments> dateAndCookIdForFindByCookForDate() {
        return Stream.of(
                Arguments.of(LocalDateTime.parse("2021-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"), 4, 2),
                Arguments.of(LocalDateTime.parse("2025-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"), 4, 0),
                Arguments.of(LocalDateTime.parse("2021-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"), 1000, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("dateAndBartenderIdForFindByBartenderForDate")
    public void findByBartenderForDate(LocalDateTime startDate, LocalDateTime endDate, int bartenderId, int expected) {
        List<OrderItem> orderItems = orderItemRepository.findByBartenderForDate(bartenderId, startDate, endDate);
        assertEquals(expected, orderItems.size());
    }

    private static Stream<Arguments> dateAndBartenderIdForFindByBartenderForDate() {
        return Stream.of(
                Arguments.of(LocalDateTime.parse("2022-11-18T08:00"), LocalDateTime.parse("2022-11-28T23:00"), 2, 1),
                Arguments.of(LocalDateTime.parse("2022-12-22T08:00"), LocalDateTime.parse("2022-11-01T11:45"), 2, 0),
                Arguments.of(LocalDateTime.parse("2022-11-18T08:00"), LocalDateTime.parse("2022-11-28T23:00"), 200, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("cookIdBartenderIdForFindByEmployee")
    public void findByEmployee(int id, int expected) {
        Page<OrderItem> orderItems = orderItemRepository.findByEmployee(PageRequest.of(0,5), id);
        List<OrderItem> orderItemList = orderItems.getContent();
        assertEquals(expected, orderItemList.size());
    }

    private static Stream<Arguments> cookIdBartenderIdForFindByEmployee() {
        return Stream.of(
                Arguments.of( 3, 2),
                Arguments.of( 2, 2),
                Arguments.of( 1, 0)
        );
    }

}
