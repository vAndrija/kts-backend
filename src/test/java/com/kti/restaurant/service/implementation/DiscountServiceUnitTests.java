package com.kti.restaurant.service.implementation;

import java.time.LocalDate;
import java.util.Optional;

import com.kti.restaurant.exception.MissingEntityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.kti.restaurant.model.Discount;
import com.kti.restaurant.repository.DiscountRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class DiscountServiceUnitTests {

	@InjectMocks
	private DiscountService discountService;

	@Mock
	private DiscountRepository discountRepository;

	@BeforeEach
	public void setup() {
		Discount discount = new Discount(10, LocalDate.parse("2021-11-19"), LocalDate.parse("2021-11-20"), false,
				null);
		discount.setId(1);

		when(discountRepository.findById(1))
				.thenReturn(Optional.of(discount));
	}
	
	@Test
	public void findById_ValidId_ExistingDiscount() throws Exception {
		Discount discount = discountService.findById(1);
		assertEquals(discount.getValue(), new Integer(10));
	}

	@Test
	public void findById_InvalidId_ThrownMissingEntityException() throws Exception {
		Assertions.assertThrows(MissingEntityException.class, () -> {
			discountService.findById(2);
		});
	}

	@Test
	public void update_ValidDiscount_ValidDiscount() throws Exception {
		Discount discountForUpdate = new Discount(15, LocalDate.parse("2021-11-21"), LocalDate.parse("2021-11-22"), true,
				null);

		when(discountRepository.save(any()))
				.thenAnswer(a -> a.getArgument(0));

		Discount discount = discountService.update(discountForUpdate, 1);

		assertEquals(discount.getValue(), new Integer(15));
		assertEquals(discount.getStartDate(), LocalDate.parse("2021-11-21"));
		assertEquals(discount.getEndDate(), LocalDate.parse("2021-11-22"));
		assertEquals(discount.getCurrent(), true);
	}

	@Test
	public void update_InvalidId_ThrownMissingEntityException() {
		Assertions.assertThrows(MissingEntityException.class, () -> {
			discountService.update(null, 2);
		});
	}

	@Test
	public void delete_ValidId_DiscountDeleted() throws Exception {
		Assertions.assertDoesNotThrow(() -> {
			discountService.delete(1);
		});
	}

	@Test
	public void delete_InvalidId_ThrownMissingEntityException() {
		Assertions.assertThrows(MissingEntityException.class, () -> {
			discountService.delete(2);
		});
	}
}
