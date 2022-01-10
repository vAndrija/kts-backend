package com.kti.restaurant.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
	
	@ParameterizedTest
	@MethodSource("provideInvalidDateToMealDrinkSalesForMonthReport")
	public void mealDrinkSalesForMonth_InvalidDate_ThrowsBadLogicException(Integer year, Integer month, String exceptionMessage) {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.mealDrinkSalesForMonth(year, month, 1);
		});
		
		assertEquals(exceptionMessage, exception.getMessage());
	}

	private static Stream<Arguments> provideInvalidDateToMealDrinkSalesForMonthReport() {
		return Stream.of(
				Arguments.of(-1, 10, badLogicYearInvalidMessage),
				Arguments.of(2021, 0, badLogicMonthInvalidMessage),
				Arguments.of(2021, 13, badLogicMonthInvalidMessage)
		);
	}

	@Test
	public void costBenefitRatioForYear_ValidYear_ReturnsCostBenefitRatioPerMonths() {
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
	public void costBenefitRatioForMonth_ValidParameters_ReturnsCostBenefitRatioPerDays() {
		List<Double> costBenefitRatio = reportService.costBenefitRatioForMonth(2021, 11);
		assertEquals(30, costBenefitRatio.size());
		assertEquals(Double.valueOf(-8167), costBenefitRatio.get(0));
		assertEquals(Double.valueOf(-5217), costBenefitRatio.get(18));
	}

	@ParameterizedTest
	@MethodSource("provideInvalidDateToCostBenefitRationForMonthReport")
	public void costBenefitRatioForMonth_InvalidDate_ThrowsBadLogicException(Integer year, Integer month, String exceptionMessage) {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.costBenefitRatioForMonth(year, month);
		});

		assertEquals(exceptionMessage, exception.getMessage());
	}

	private static Stream<Arguments> provideInvalidDateToCostBenefitRationForMonthReport() {
		return Stream.of(
				Arguments.of(-1, 10, badLogicYearInvalidMessage),
				Arguments.of(2021, 0, badLogicMonthInvalidMessage)
		);
	}

	@ParameterizedTest
	@MethodSource("provideValidParamsForPreparationTimeForYearReport")
	public void preparationTimeForYear_ValidParameters_ReturnsPreparationTimePerMonthsForBartender(
			Integer year, Integer employeId, Integer sizeOfReturnedList, Integer valueOfOneElementInList,
			Integer indexOfOneElementInList, Integer valueOfAnotherElementInList, Integer indexOfAnotherElementInList
	) {
		List<Integer> preparationTime = reportService.preparationTimeForYear(year, employeId);
		assertEquals(sizeOfReturnedList, preparationTime.size());
		assertEquals(valueOfOneElementInList, preparationTime.get(indexOfOneElementInList));
		assertEquals(valueOfAnotherElementInList, preparationTime.get(indexOfAnotherElementInList));
	}

	private static Stream<Arguments> provideValidParamsForPreparationTimeForYearReport() {
		return Stream.of(
				Arguments.of(2021, 2, 12, 0, 0, 2, 10),
				Arguments.of(2021, 4, 12, 0, 0, 18, 10)
		);
	}

	@Test
	public void preparationTimeForYear_InvalidUserId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reportService.preparationTimeForYear(2021, 25);
		});

		assertEquals(missingUserMessage, exception.getMessage());
	}

	@Test
	public void preparationTimeForYear_InvalidUserRole_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.preparationTimeForYear(2021, 1);
		});

		assertEquals(badLogicInvalidUserRole, exception.getMessage());
	}

	@Test 
	public void preparationTimeForYear_InvalidYear_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.preparationTimeForYear(-1, 1);
		});

		assertEquals(badLogicYearInvalidMessage, exception.getMessage());
	}
	
	@Test 
	public void mealDrinkCostsForYear_ValidParameters_ReturnsMealDrinkCostsForYear() {
		List<Double> costsPerMonths = reportService.mealDrinkCostsForYear(2021);
		
		assertEquals(12, costsPerMonths.size());
		assertEquals(1860.00, costsPerMonths.get(10));
		assertEquals(0.00, costsPerMonths.get(1));
	}
	
	@Test
	public void mealDrinkCostsForYear_InvalidYear_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.mealDrinkCostsForYear(-2);
		});
		
		assertEquals(badLogicYearInvalidMessage, exception.getMessage());
	}
	
	@Test
	public void mealDrinkCostsForMonth_ValidParameters_ReturnsCostsPerDay() {
		List<Double> costsPerDay = reportService.mealDrinkCostsForMonth(2021, 11);
		
		assertEquals(30, costsPerDay.size());
		assertEquals(1860.00, costsPerDay.get(18));
	}
	
	@ParameterizedTest
	@MethodSource("provideInvalidDateForMealDrinkCostForMonthReport")
	public void mealDrinkCostsForMonth_InvalidDate_ThrowsBadLogicException(Integer year, Integer month, String exceptionMessage) {
		Exception exception = assertThrows(BadLogicException.class, () ->{
			reportService.mealDrinkCostsForMonth(year, month);
		});
		
		assertEquals(exceptionMessage, exception.getMessage());
	}

	private static Stream<Arguments> provideInvalidDateForMealDrinkCostForMonthReport() {
		return Stream.of(
				Arguments.of(-1, 1, badLogicYearInvalidMessage),
				Arguments.of(2021, 0, badLogicMonthInvalidMessage),
				Arguments.of(2021, 13, badLogicMonthInvalidMessage)
		);
	}
	
	@ParameterizedTest
	@MethodSource("provideValidParametersForPreparationTimeForMonthReport")
	public void preparationTimeForMonth_ValidParameters_ReturnsPreparationTimePerDayForCook(
			Integer year, Integer month, Integer emploeeId, Integer sizeOfReturnedList, Integer valueOfOneElementInReturnedList,
			Integer indexOfOneElementInReturnedList
	) {
		List<Integer> minutesPerMonth = reportService.preparationTimeForMonth(year, month, emploeeId);
		
		assertEquals(sizeOfReturnedList, minutesPerMonth.size());
		assertEquals(valueOfOneElementInReturnedList, minutesPerMonth.get(indexOfOneElementInReturnedList));
	}

	private static Stream<Arguments> provideValidParametersForPreparationTimeForMonthReport() {
		return Stream.of(
				Arguments.of(2021, 11, 4, 30, 18, 18),
				Arguments.of(2021, 11, 2, 30, 2, 18)
		);
	}
	
	@ParameterizedTest
	@MethodSource("provideInvalidDateForPreparationTimeForMonthReport")
	public void preparationTimeForMonth_InvalidDate_ThrowsBadLogicException(Integer year, Integer month, String exceptionMessage) {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.preparationTimeForMonth(year, month, 1);
		});
		
		assertEquals(exceptionMessage, exception.getMessage());
	}

	private static Stream<Arguments> provideInvalidDateForPreparationTimeForMonthReport() {
		return Stream.of(
				Arguments.of(-1, 1, badLogicYearInvalidMessage),
				Arguments.of(2021, 0, badLogicMonthInvalidMessage),
				Arguments.of(2021, 13, badLogicMonthInvalidMessage)
		);
	}
	
	@Test
	public void preparationTimeForMonth_InvalidUserId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reportService.preparationTimeForMonth(2021, 1, 15);
		});
		
		assertEquals(missingUserMessage, exception.getMessage());
	}
	
	@Test
	public void preparationTimeForMonth_InvalidUserId_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.preparationTimeForMonth(2021, 1, 1);
		});
		
		assertEquals(badLogicInvalidUserRole, exception.getMessage());
	}
	
	
	
 
}
