package com.kti.restaurant.service.implementation;

import java.time.LocalDate;
import java.util.Optional;

import com.kti.restaurant.exception.BadLogicException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
		when(discountRepository.save(any())).thenAnswer(a -> a.getArgument(0));
	}

	@Test
	public void create_ValidDiscount_CreatedDiscount() throws Exception {
		Discount discount = discountService.create(new Discount(10, LocalDate.parse("2021-11-19"), LocalDate.parse("2021-11-20"), false,
				null));

		assertEquals(Integer.valueOf(10), discount.getValue());
		assertEquals(LocalDate.parse("2021-11-19"), discount.getStartDate());
		assertEquals(LocalDate.parse("2021-11-20"), discount.getEndDate());
		assertEquals(false, discount.getCurrent());
	}

	@Test
	public void create_DiscountWithInvalidDates_ThrowsBadLogicException() {
		assertThrows(BadLogicException.class, () -> {
			Discount discount = discountService.create(new Discount(10, LocalDate.parse("2021-11-20"), LocalDate.parse("2021-11-19"), false,
					null));
		});
	}
	
	@Test
	public void findById_ValidId_ExistingDiscount() throws Exception {
		Discount discount = discountService.findById(1);
		assertEquals(Integer.valueOf(10), discount.getValue());
	}

	@Test
	public void findById_InvalidId_ThrowsMissingEntityException() throws Exception {
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

		assertEquals(Integer.valueOf(15), discount.getValue());
		assertEquals(LocalDate.parse("2021-11-21"), discount.getStartDate());
		assertEquals(LocalDate.parse("2021-11-22"), discount.getEndDate());
		assertEquals(true, discount.getCurrent());
		verify(discountRepository, times(1)).findById(1);
		verify(discountRepository, times(1)).save(any());
	}

	@Test
	public void update_InvalidId_ThrowsMissingEntityException() {
		Assertions.assertThrows(MissingEntityException.class, () -> {
			discountService.update(null, 2);
		});
		verify(discountRepository, times(1)).findById(2);
		verify(discountRepository, times(0)).save(any());
	}

	@Test
	public void update_InvalidDates_ThrowsMissingEntityException() {
		Assertions.assertThrows(BadLogicException.class, () -> {
			discountService.update(new Discount(null, LocalDate.parse("2022-11-05"), LocalDate.parse("2021-11-05"), null, null), 1);
		});
		verify(discountRepository, times(1)).findById(1);
		verify(discountRepository, times(0)).save(any());
	}

	@Test
	public void delete_ValidId_DiscountDeleted() throws Exception {
		Assertions.assertDoesNotThrow(() -> {
			discountService.delete(1);
		});
		verify(discountRepository, times(1)).findById(1);
		verify(discountRepository, times(1)).deleteById(1);
	}

	@Test
	public void delete_InvalidId_ThrowsMissingEntityException() {
		Assertions.assertThrows(MissingEntityException.class, () -> {
			discountService.delete(2);
		});
		verify(discountRepository, times(1)).findById(2);
		verify(discountRepository, times(0)).deleteById(1);
	}
}
