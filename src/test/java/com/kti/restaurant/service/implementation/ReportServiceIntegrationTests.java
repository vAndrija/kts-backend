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
	private static final String missingUserMessage = "User with given id does not exist in the system.";
	private static final String badLogicInvalidUserRole = "This report can only show data of cooks and bartenders.";
	
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

	@Test
	public void costBenefitRatioForYear_ValidYear_CostBenefitRatioPerMonths() {
		List<Double> costBenefitRation = reportService.costBenefitRatioForYear(2021);
		assertEquals(12, costBenefitRation.size());
		assertEquals(Double.valueOf(0), costBenefitRation.get(0));
		assertEquals(Double.valueOf(-135000), costBenefitRation.get(1));
		assertEquals(Double.valueOf(-195000), costBenefitRation.get(8));
		assertEquals(Double.valueOf(-242050), costBenefitRation.get(10));
		assertEquals(Double.valueOf(-464000), costBenefitRation.get(11));
	}

	@Test
	public void costBenefitRatioForYear_InvalidYear_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.costBenefitRatioForYear(-1);
		});

		assertEquals(badLogicYearInvalidMessage, exception.getMessage());
	}

	@Test
	public void costBenefitRatioForMonth_ValidParameters_CostBenefitRatioPerDays() {
		List<Double> costBenefitRatio = reportService.costBenefitRatioForMonth(2021, 11);
		assertEquals(30, costBenefitRatio.size());
		assertEquals(Double.valueOf(-8167), costBenefitRatio.get(0));
		assertEquals(Double.valueOf(-5217), costBenefitRatio.get(18));
	}

	@Test
	public void costBenefitRatioForMonth_InvalidYear_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.costBenefitRatioForMonth(-1, 10);
		});

		assertEquals(badLogicYearInvalidMessage, exception.getMessage());
	}

	@Test
	public void costBenefitRatioForMonth_InvalidMonth_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.costBenefitRatioForMonth(2021, 0);
		});

		assertEquals(badLogicMonthInvalidMessage, exception.getMessage());
	}

	@Test
	public void preparationTimeForYear_ValidParameters_PreparationTimePerMonthsForBartender() {
		List<Integer> preparationTime = reportService.preparationTimeForYear(2021, 2);
		assertEquals(12, preparationTime.size());
		assertEquals(0, preparationTime.get(0));
		assertEquals(2, preparationTime.get(10));
	}

	@Test
	public void preparationTimeForYear_ValidParameters_PreparationTimePerMonthsForCook() {
		List<Integer> preparationTime = reportService.preparationTimeForYear(2021, 4);
		assertEquals(12, preparationTime.size());
		assertEquals(0, preparationTime.get(0));
		assertEquals(18, preparationTime.get(10));
	}

	@Test
	public void preparationTimeForYear_InvalidUserId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reportService.preparationTimeForYear(2021, 25);
		});

		assertEquals(missingUserMessage, exception.getMessage());
	}

	@Test void preparationTimeForYear_InvalidUserRole_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.preparationTimeForYear(2021, 1);
		});

		assertEquals(badLogicInvalidUserRole, exception.getMessage());
	}

	@Test void preparationTimeForYear_Invalidear_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.preparationTimeForYear(-1, 1);
		});

		assertEquals(badLogicYearInvalidMessage, exception.getMessage());
	}

}
