package com.kti.restaurant.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.kti.restaurant.model.*;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.kti.restaurant.exception.BadLogicException;
import com.kti.restaurant.exception.MissingEntityException;
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
	private static final String missingEntityExceptionMessage = "User with given id does not exist in the system.";

	@BeforeEach
	public void setUp() {
		Order order = new Order(OrderStatus.ORDERED, LocalDateTime.parse("2021-10-20T10:00"), 1200.00);
		order.setId(1);

		OrderItem orderItem = new OrderItem(2, "Nema napomene", OrderItemStatus.ORDERED, 1, order,
				new MenuItem("", "", MenuItemCategory.BREAKFAST, MenuItemType.DRINK, 20), null, null);
		orderItem.setId(1);

		List<OrderItem> orderItems = new ArrayList<OrderItem>();
		orderItems.add(orderItem);

		when(orderItemService.findOrderItemsInPeriod(any(), any())).thenReturn(orderItems);
		when(orderItemService.findOrderItemsInPeriodForMenuItem(any(), any(), any())).thenReturn(orderItems);

		PriceItem priceItem = new PriceItem(Double.valueOf(150), LocalDate.now(), LocalDate.now(),
				null, true, Double.valueOf(100));
		when(priceItemService.findPriceForDate(any(), any())).thenReturn(priceItem);

		List<User> users = new ArrayList<>();
		users.add(new Bartender());
		users.add(new Cook());
		users.add(new Manager());
		users.add(new Waiter());

		when(userService.findAll()).thenReturn(users);

		Salary salary = new Salary(Double.valueOf(35000), LocalDate.parse("2020-08-08"), LocalDate.parse("2022-08-08"));
		when(salaryService.findSalaryForDate(any(), any())).thenReturn(salary);

		Bartender bartender = new Bartender();
		bartender.setId(1);
		List<Role> bartenderRoles = new ArrayList<>();
		Role bartenderRole = new Role();
		bartenderRole.setName("ROLE_BARTENDER");
		bartenderRoles.add(bartenderRole);
		bartender.setRoles(bartenderRoles);
		when(userService.findById(1)).thenReturn(bartender);

		Cook cook = new Cook();
		cook.setId(2);
		List<Role> cookRoles = new ArrayList<>();
		Role cookRole = new Role();
		cookRole.setName("ROLE_COOK");
		cookRoles.add(cookRole);
		cook.setRoles(cookRoles);
		when(userService.findById(2)).thenReturn(cook);

		when(orderItemService.findByCook(any(), any(), any())).thenReturn(orderItems);
		when(orderItemService.findByBartender(any(), any(), any())).thenReturn(orderItems);
	}
	
	@Test
	public void mealDrinkSalesForYear_ValidParameters_ReturnsSalesPerMonth() throws Exception {
		when(menuItemService.findById(1)).thenReturn(new MenuItem());
		
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
		when(menuItemService.findById(1)).thenReturn(new MenuItem());
		
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

	@Test
	public void mealDrinkCostsForYear_ValidYear_ReturnsCostsPerMonths() {
		List<Double> costs = reportService.mealDrinkCostsForYear(2021);
		assertEquals(12, costs.size());
		assertEquals(0, costs.get(0));
		assertEquals(Double.valueOf(200), costs.get(9));
	}

	@Test
	public void mealDrinkCostsForYear_InvalidYear_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.mealDrinkCostsForYear(-1);
		});

		assertEquals(badLogicYearInvalidMessage, exception.getMessage());
	}

	@Test
	public void mealDrinkCostsForMonth_ValidParameters_ReturnsCostsPerDay() {
		List<Double> costs = reportService.mealDrinkCostsForMonth(2021, 10);
		assertEquals(31, costs.size());
		assertEquals(Double.valueOf(200), costs.get(19));
	}

	@Test
	public void mealDrinkCostsForMonth_InvalidYear_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.mealDrinkCostsForMonth(-1, 11);
		});

		assertEquals(badLogicYearInvalidMessage, exception.getMessage());
	}

	@Test
	public void mealDrinkCostsForMonth_InvalidMonth_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.mealDrinkCostsForMonth(2021, -11);
		});

		assertEquals(badLogicMonthInvalidMessage, exception.getMessage());
	}

	@Test
	public void costBenefitRatioForYear_ValidYear_ReturnsCostBenefitRatioPerMonths() {
		List<Double> costBenefitRatio = reportService.costBenefitRatioForYear(2021);
		assertEquals(12, costBenefitRatio.size());
		assertEquals(Double.valueOf(-140000), costBenefitRatio.get(0));
		assertEquals(Double.valueOf(-139900), costBenefitRatio.get(9));
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
		List<Double> costBenefitRatio = reportService.costBenefitRatioForMonth(2021, 10);
		assertEquals(31, costBenefitRatio.size());
		assertEquals(Double.valueOf(-4516), costBenefitRatio.get(0));
		assertEquals(Double.valueOf(-4416), costBenefitRatio.get(19));
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
			reportService.costBenefitRatioForMonth(2021, -11);
		});

		assertEquals(badLogicMonthInvalidMessage, exception.getMessage());
	}

	@Test
	public void preparationTimeForYear_ValidParameters_ReturnsPreparationTimesPerMonthsForBartender() {
		List<Integer> preparationTimes = reportService.preparationTimeForYear(2021, 1);
		assertEquals(12, preparationTimes.size());
		assertEquals(0, preparationTimes.get(0));
		assertEquals(40, preparationTimes.get(9));
	}

	@Test
	public void preparationTimeForYear_ValidParameters_ReturnsPreparationTimesPerMonthsForCook() {
		List<Integer> preparationTimes = reportService.preparationTimeForYear(2021, 2);
		assertEquals(12, preparationTimes.size());
		assertEquals(0, preparationTimes.get(0));
		assertEquals(40, preparationTimes.get(9));
	}

	@Test
	public void preparationTimeForYear_InvalidYear_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.preparationTimeForYear(-1, 1);
		});

		assertEquals(badLogicYearInvalidMessage, exception.getMessage());
	}

	@Test
	public void preparationTimeForYear_InvalidUserId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reportService.preparationTimeForYear(2021, 10);
		});

		assertEquals(missingEntityExceptionMessage, exception.getMessage());
	}

	@Test
	public void preparationTimeForMonth_ValidParameters_ReturnsPreparationTimesPerDaysForBartender() {
		List<Integer> preparationTimes = reportService.preparationTimeForMonth(2021, 10, 1);
		assertEquals(31, preparationTimes.size());
		assertEquals(0, preparationTimes.get(0));
		assertEquals(40, preparationTimes.get(19));
	}

	@Test
	public void preparationTimeForMonth_ValidParameters_ReturnsPreparationTimesPerDaysForCook() {
		List<Integer> preparationTimes = reportService.preparationTimeForMonth(2021, 10, 2);
		assertEquals(31, preparationTimes.size());
		assertEquals(0, preparationTimes.get(0));
		assertEquals(40, preparationTimes.get(19));
	}

	@Test
	public void preparationTimeForMonth_InvalidYear_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.preparationTimeForMonth(-1, 1, 1);
		});

		assertEquals(badLogicYearInvalidMessage, exception.getMessage());
	}

	@Test
	public void preparationTimeForMont_InvalidMonth_ThrowsBadLogicException() {
		Exception exception = assertThrows(BadLogicException.class, () -> {
			reportService.preparationTimeForMonth(2021, -11, 1);
		});

		assertEquals(badLogicMonthInvalidMessage, exception.getMessage());
	}

	@Test
	public void preparationTimeForMonth_InalidUserId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reportService.preparationTimeForMonth(2021, 10, 10);
		});

		assertEquals(missingEntityExceptionMessage, exception.getMessage());
	}
}
