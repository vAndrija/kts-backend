package com.kti.restaurant.repository;

import java.time.LocalDate;

import static com.kti.restaurant.constants.SalaryConstants.SALARY_VALUE;
import static org.junit.Assert.assertEquals;

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
		assertEquals(SALARY_VALUE, salary.getValue());
	}
	
	@Test
	public void findSalaryForDate_InvalidDate_Null() {
		Salary salary = salaryRepository.findSalaryForDate(LocalDate.parse("2021-11-15"), 1);
		assertEquals(salary, null);
	}
	
	@Test
	public void findSalaryForDate_InvalidUserIdKey_Null() {
		Salary salary = salaryRepository.findSalaryForDate(LocalDate.parse("2021-11-19"), 100);
		assertEquals(salary, null);
	}
}
