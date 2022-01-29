package com.kti.restaurant.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.kti.restaurant.model.Salary;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class SalaryRepositoryTests {

	@Autowired
	private SalaryRepository salaryRepository;


	@ParameterizedTest
	@MethodSource("provideValidDatesForFindSalaryForDate")
	public void findSalaryForDate_ReturnsSalaries(LocalDate date, int userId, double expected) {
		Salary salary = salaryRepository.findSalaryForDate(date, userId);
		assertEquals(expected, salary.getValue());
	}

	private static Stream<Arguments> provideValidDatesForFindSalaryForDate() {
		return Stream.of(
				Arguments.of(LocalDate.parse("2021-11-19"), 1, Double.valueOf(45000.00))
		);
	}

	@ParameterizedTest
	@MethodSource("provideInvalidDatesForFindSalaryForDate")
	public void findSalaryForDateInvalid_ReturnsSalaries(LocalDate date, int userId, Object expected) {
		Salary salary = salaryRepository.findSalaryForDate(date, userId);
		assertEquals(expected, salary);
	}

	private static Stream<Arguments> provideInvalidDatesForFindSalaryForDate() {
		return Stream.of(
				Arguments.of(LocalDate.parse("2021-11-15"), 1, null),
				Arguments.of(LocalDate.parse("2021-11-19"), 100, null)
		);
	}
}
