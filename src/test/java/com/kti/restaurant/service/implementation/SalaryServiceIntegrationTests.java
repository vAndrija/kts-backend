package com.kti.restaurant.service.implementation;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Salary;

@SpringBootTest
@Transactional
public class SalaryServiceIntegrationTests {

	@Autowired
	private SalaryService salaryService;
	
	private static final String message = "Salary with given id does not exist in the system.";
	
	@Test
	public void findAll_ReturnsValidSalaries() {
		List<Salary> salaries = salaryService.findAll();
		
		assertEquals(8, salaries.size());
	}
	
	@Test
	public void findById_ValidId_ReturnsValidSalary() throws Exception {
		Salary salary = salaryService.findById(1);
		
		assertEquals(Double.valueOf(45000.00), salary.getValue());
		assertEquals(LocalDate.parse("2021-11-18"), salary.getStartDate());
		assertEquals(LocalDate.parse("2022-11-18"), salary.getEndDate());
		assertEquals(Integer.valueOf(1), salary.getUser().getId());
	}
	
	@Test
	public void findById_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			salaryService.findById(10);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	@Rollback
	public void create_ValidSalary_ReturnsValidSalary() throws Exception {
		Salary salary = salaryService.create(new Salary(46000.00, LocalDate.parse("2021-10-10"), LocalDate.parse("2022-10-10")));
		
		assertEquals(Integer.valueOf(9), salary.getId());
		assertEquals(Double.valueOf(46000.00), salary.getValue());
		assertEquals(LocalDate.parse("2021-10-10"), salary.getStartDate());
		assertEquals(LocalDate.parse("2022-10-10"), salary.getEndDate());

	}
	
	@Test
	@Rollback
	public void update_ValidId_ReturnsUpdatedSalary() throws Exception {
		Salary salaryToUpdate = new Salary(46000.00, LocalDate.parse("2021-10-10"), LocalDate.parse("2022-10-10"));
		
		Salary salary = salaryService.update(salaryToUpdate, 1);
		
		assertEquals(salaryToUpdate.getValue(), salary.getValue());
		assertEquals(salaryToUpdate.getStartDate(), salary.getStartDate());
		assertEquals(salaryToUpdate.getEndDate(), salary.getEndDate());
	}
	
	@Test
	public void update_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			salaryService.update(null, 10);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	@Rollback
	public void delete_ValidId() throws Exception {
		salaryService.delete(1);
		
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			salaryService.findById(1);
		});

		assertEquals(message, exception.getMessage());
	}
	
	@Test
	public void delete_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			salaryService.delete(10);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	public void findSalaryForDate_ValidDateAndUserId_ReturnsValidSalary() {
		Salary salary = salaryService.findSalaryForDate(LocalDate.parse("2021-11-19"), 1);
		
		assertEquals(Double.valueOf(45000.00), salary.getValue());
		assertEquals(LocalDate.parse("2021-11-18"), salary.getStartDate());
		assertEquals(LocalDate.parse("2022-11-18"), salary.getEndDate());
		assertEquals(Integer.valueOf(1), salary.getUser().getId());
	}
	
	@Test
	public void findSalaryForDate_InvalidDate_ReturnsNull() {
		assertEquals(null, salaryService.findSalaryForDate(LocalDate.parse("2021-10-19"), 1));
	}
	
	@Test
	public void findSalaryForDate_InvalidUserId_ReturnsNull() {
		assertEquals(null, salaryService.findSalaryForDate(LocalDate.parse("2021-11-19"), 10));
	}
	
	@Test
	public void findSalaryForDate_InvalidDateAndUserId_ReturnsNull() {
		assertEquals(null, salaryService.findSalaryForDate(LocalDate.parse("2021-01-01"), 10));
	}
}
