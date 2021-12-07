package com.kti.restaurant.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;

@SpringBootTest
@Transactional
public class ReportServiceIntegrationTests {

	@Autowired
	private ReportService reportService;
	
	private static final String missingMenuItemMessage = "Menu item with given id does not exist in the system.";
	private static final String badLogicYearInvalidMessage = "Year cannot be negative value.";
	private static final String badLogicMonthInvalidMessage = "Month needs to be in range 1 and 12";
	
	@Test
	public void mealDrinkSalesForYear_ValidParameters_ReturnsSalesPerMonth() throws Exception {		
		List<Integer> salesPerMonth = reportService.mealDrinkSalesForYear(2021, 1);
		
		assertEquals(12, salesPerMonth.size());
		assertEquals(3, salesPerMonth.get(10));
		assertEquals(0, salesPerMonth.get(0));
	}
	
	@Test 
	public void mealDrinkSalesForYear_InvalidMenuItemId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reportService.mealDrinkSalesForYear(2021, 100);
		});
		
		assertEquals(missingMenuItemMessage, exception.getMessage());
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
		List<Integer> salesPerDay = reportService.mealDrinkSalesForMonth(2021, 11, 1);
		
		assertEquals(30, salesPerDay.size());
		assertEquals(3, salesPerDay.get(18));
		assertEquals(0, salesPerDay.get(0));
	}
	
	@Test 
	public void mealDrinkSalesForMonth_InvalidMenuItemId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reportService.mealDrinkSalesForMonth(2021, 10, 100);
		});
		
		assertEquals(missingMenuItemMessage, exception.getMessage());
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
