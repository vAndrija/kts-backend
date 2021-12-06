package com.kti.restaurant.repository;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.kti.restaurant.model.OrderItem;


@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class OrderItemRepositoryTest {
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Test
	public void findOrderItemsByDate_ValidDates_ExistingOrderItems() {
		List<OrderItem> orderItems = orderItemRepository.findOrderItemsByDate(LocalDateTime.parse("2022-11-18T08:00"), LocalDateTime.parse("2022-11-19T08:00"));
		assertEquals(2, orderItems.size());
	}
	
	@Test
	public void findOrderItemsByDate_InvalidDates_EmptyList() {
		List<OrderItem> orderItems = orderItemRepository.findOrderItemsByDate(LocalDateTime.parse("2022-11-18T14:00"), LocalDateTime.parse("2021-01-01T14:00"));
		assertEquals(0, orderItems.size());
	}
	
	@Test
	public void findSalesForMenuItem_ValidDates_ValidMenuItemKey_ExistingOrderItems() {
		List<OrderItem> orderItems = orderItemRepository.findSalesForMenuItem(LocalDateTime.parse("2021-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"), 1);
		assertEquals(2, orderItems.size());
	}
	
	@Test
	public void findSalesForMenuItem_InvalidDates_ValidMenuItemKey_EmptyList() {
		List<OrderItem> orderItems = orderItemRepository.findSalesForMenuItem(LocalDateTime.parse("2022-11-18T08:00"), LocalDateTime.parse("2022-11-01T23:00"), 1);
		assertEquals(0, orderItems.size());
	}
	
	@Test
	public void findSalesForMenuItem_ValidDates_InvalidMenuItemKey_EmptyList() {
		List<OrderItem> orderItems = orderItemRepository.findSalesForMenuItem(LocalDateTime.parse("2021-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"), 1000);
		assertEquals(0, orderItems.size());
	}
	
	@Test
	public void findByCookForDate_ValidDate_ValidCookId_ExistingOrderItems() {
		List<OrderItem> orderItems = orderItemRepository.findByCookForDate(4,LocalDateTime.parse("2021-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"));
		assertEquals(2, orderItems.size());
	}

	@Test
	public void findByCookForDate_InvalidDate_ValidCookId_EmptyList() {
		List<OrderItem> orderItems = orderItemRepository.findByCookForDate(4,LocalDateTime.parse("2025-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"));
		assertEquals(0, orderItems.size());
	}
	
	@Test
	public void findByCookForDate_ValidDate_InvalidCookId_EmptyList() {
		List<OrderItem> orderItems = orderItemRepository.findByCookForDate(1000,LocalDateTime.parse("2021-11-18T08:00"), LocalDateTime.parse("2021-11-19T23:00"));
		assertEquals(0, orderItems.size());
	}
	
	@Test
	public void findByBartenderForDate_ValidDate_ValidBartenderId_ExistingOrderItems() {
		List<OrderItem> orderItems = orderItemRepository.findByBartenderForDate(2,LocalDateTime.parse("2022-11-18T08:00"), LocalDateTime.parse("2022-11-28T23:00"));
		assertEquals(1, orderItems.size());
	}

	@Test
	public void findByBartenderForDate_InvalidDate_ValidBartenderId_EmptyList() {
		List<OrderItem> orderItems = orderItemRepository.findByBartenderForDate(2,LocalDateTime.parse("2022-12-22T08:00"), LocalDateTime.parse("2022-11-01T11:45"));
		assertEquals(0, orderItems.size());
	}
	
	@Test
	public void findByBartenderForDate_ValidDate_InvalidBartenderId_EmptyList() {
		List<OrderItem> orderItems = orderItemRepository.findByBartenderForDate(200,LocalDateTime.parse("2022-11-18T08:00"), LocalDateTime.parse("2022-11-28T23:00"));
		assertEquals(0, orderItems.size());
	}

}
