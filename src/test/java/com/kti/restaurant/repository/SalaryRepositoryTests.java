package com.kti.restaurant.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.kti.restaurant.model.Salary;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class SalaryRepositoryTests {

	@Autowired
	private SalaryRepository salaryRepository;
	
	@Test
	public void findSalaryForDate_ValidDate_ExistingUser() {
		Salary salary = salaryRepository.findSalaryForDate(LocalDate.parse("2021-11-19"), 1);
		assertEquals(Double.valueOf(45000.00), salary.getValue());
	}
	
	@Test
	public void findSalaryForDate_InvalidDate_Null() {
		Salary salary = salaryRepository.findSalaryForDate(LocalDate.parse("2021-11-15"), 1);
		assertEquals(null, salary);
	}
	
	@Test
	public void findSalaryForDate_InvalidUserIdKey_Null() {
		Salary salary = salaryRepository.findSalaryForDate(LocalDate.parse("2021-11-19"), 100);
		assertEquals(null, salary);
	}
}
