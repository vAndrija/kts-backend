package com.kti.restaurant.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.model.enums.OrderItemStatus;
import com.kti.restaurant.model.enums.OrderStatus;
import com.kti.restaurant.service.UserService;
import com.kti.restaurant.service.contract.IOrderItemService;
import com.kti.restaurant.service.contract.IPriceItemService;
import com.kti.restaurant.service.contract.ISalaryService;

@SpringBootTest
public class ReportServiceUnitTests {

	@InjectMocks
	private ReportService reportService;
	
	@Mock
	private IOrderItemService orderItemService;
	
	@Mock
    private IPriceItemService priceItemService;
	
	@Mock
    private ISalaryService salaryService;
	
	@Mock
    private UserService userService;
	
	@Mock
	private MenuItemService menuItemService;
	
	private static final String badLogicYearInvalidMessage = "Year cannot be negative value.";
	private static final String badLogicMonthInvalidMessage = "Month needs to be in range 1 and 12";
	
	@Test
	public void mealDrinkSalesForYear_ValidParameters_ReturnsSalesPerMonth() throws Exception {
		
		Order order = new Order(OrderStatus.ORDERED, LocalDateTime.parse("2021-10-20T10:00"), 1200.00);
		order.setId(1);
		
		OrderItem orderItem = new OrderItem(2, "Nema napomene", OrderItemStatus.ORDERED, 1, order, new MenuItem(), null, null);
		orderItem.setId(1);
		
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		orderItems.add(orderItem);
		
		when(menuItemService.findById(1)).thenReturn(new MenuItem());
		when(orderItemService.findOrderItemsInPeriodForMenuItem(any(), any(), any())).thenReturn(orderItems);
		
		List<Integer> salesPerMonth = reportService.mealDrinkSalesForYear(2021, 1);
		
		assertEquals(12, salesPerMonth.size());
		assertEquals(2, salesPerMonth.get(9));
		assertEquals(0, salesPerMonth.get(0));
	}
	
	
	@Test 
	public void mealDrinkSalesForYear_InvalidMenuItemId_ThrowsMissingEntityException() throws Exception {
		when(menuItemService.findById(100)).thenThrow(MissingEntityException.class);
		
		assertThrows(MissingEntityException.class, () -> {
			reportService.mealDrinkSalesForYear(2021, 100);
		});
	} 
	
	@Test
	public void mealDrinkSalesForYear_InvalidYear_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.mealDrinkSalesForYear(-1, 1);
		});
		
		assertEquals(badLogicYearInvalidMessage, exception.getMessage());
	}
	
	@Test
	public void mealDrinkSalesForMonth_ValidParameters_ReturnsSalesPerDay() throws Exception {
		
		Order order = new Order(OrderStatus.ORDERED, LocalDateTime.parse("2021-10-20T10:00"), 1200.00);
		order.setId(1);
		
		OrderItem orderItem = new OrderItem(2, "Nema napomene", OrderItemStatus.ORDERED, 1, order, new MenuItem(), null, null);
		orderItem.setId(1);
		
		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		orderItems.add(orderItem);
		
		when(menuItemService.findById(1)).thenReturn(new MenuItem());
		when(orderItemService.findOrderItemsInPeriodForMenuItem(any(), any(), any())).thenReturn(orderItems);
		
		List<Integer> salesPerDay = reportService.mealDrinkSalesForMonth(2021, 10, 1);
		
		assertEquals(31, salesPerDay.size());
		assertEquals(2, salesPerDay.get(19));
		assertEquals(0, salesPerDay.get(0));
	}
	
	@Test 
	public void mealDrinkSalesForMonth_InvalidMenuItemId_ThrowsMissingEntityException() throws Exception {
		when(menuItemService.findById(1)).thenThrow(MissingEntityException.class);
		
		assertThrows(MissingEntityException.class, () -> {
			reportService.mealDrinkSalesForMonth(2021, 10, 1);
		});
	} 
	
	@Test
	public void mealDrinkSalesForMonth_InvalidYear_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.mealDrinkSalesForMonth(-1, 10, 1);
		});
		
		assertEquals(badLogicYearInvalidMessage, exception.getMessage());
	}
	
	@Test
	public void mealDrinkSalesForMonth_InvalidMonth_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.mealDrinkSalesForMonth(2021, 0, 1);
		});
		
		assertEquals(badLogicMonthInvalidMessage, exception.getMessage());
	}
}
