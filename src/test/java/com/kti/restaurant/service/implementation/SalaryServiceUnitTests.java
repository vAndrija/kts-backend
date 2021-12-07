package com.kti.restaurant.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.repository.SalaryRepository;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class SalaryServiceUnitTests {

	@InjectMocks
	private SalaryService salaryService;
	
	@Mock
	private SalaryRepository salaryRepository;
	
	private static final String message = "Salary with given id does not exist in the system.";
	
	@BeforeEach
	private void setup() {
		Salary salary = new Salary(45000.00, LocalDate.parse("2021-10-10"), LocalDate.parse("2022-10-10"));
		salary.setId(1);
		
		when(salaryRepository.findById(1)).thenReturn(Optional.of(salary));
	}
	
	@Test
	public void findById_ValidId_ReturnsValidSalary() throws Exception {
		Salary salary = salaryService.findById(1);
		
		assertEquals(salary.getId(), Integer.valueOf(1));
		assertEquals(salary.getValue(), Double.valueOf(45000.00));
		assertEquals(salary.getStartDate(), LocalDate.parse("2021-10-10"));
		assertEquals(salary.getEndDate(), LocalDate.parse("2022-10-10"));
	}
	
	@Test
	public void findById_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			salaryService.findById(10);
		});
		
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	public void update_ValidNotification_ReturnsUpdatedNotification() throws Exception {
		Salary salaryToUpdate = new Salary(56000.00, LocalDate.parse("2021-10-10"), LocalDate.parse("2022-10-10"));
		salaryToUpdate.setId(1);
		
		when(salaryRepository.save(any())).thenReturn(salaryToUpdate);

		Salary salary = salaryService.update(salaryToUpdate, 1);
		assertEquals(salary.getId(), salaryToUpdate.getId());
		assertEquals(salary.getValue(), salaryToUpdate.getValue());
		assertEquals(salary.getStartDate(), salaryToUpdate.getStartDate());
		assertEquals(salary.getEndDate(), salaryToUpdate.getEndDate());
	}
	
	@Test
	public void update_InvalidId_ThrowsMissingEntityException() {
		when(salaryRepository.save(any())).thenReturn(Optional.empty());
		
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			salaryService.update(null, 10);
		});
		
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	public void delete_ValidId() throws Exception {
		salaryService.delete(1);
		verify(salaryRepository, times(1)).findById(1);
		verify(salaryRepository, times(1)).deleteById(1);
	}
	
	@Test
	public void delete_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			salaryService.delete(10);
		});
		
		assertEquals(exception.getMessage(), message);
	}
}
