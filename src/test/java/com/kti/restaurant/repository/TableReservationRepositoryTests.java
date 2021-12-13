package com.kti.restaurant.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.kti.restaurant.model.TableReservation;

@DataJpaTest
public class TableReservationRepositoryTests {
	
	@Autowired
	private TableReservationRepository reservationRepository;
	
	@ParameterizedTest
	@MethodSource("findTableReservationInDateRangeForTable")
	public void getTableReservationByDateAndTableId(Integer id, LocalDateTime startDate, LocalDateTime endDate, int expected) {
		List<TableReservation> reservations = reservationRepository.getTableReservationByDateAndTableId(id, startDate, endDate);
		
		assertEquals(expected, reservations.size());
	}
	
	private static Stream<Arguments> findTableReservationInDateRangeForTable() {
		
		return Stream.of(
				Arguments.of(1, LocalDateTime.parse("2021-11-18T00:00"), LocalDateTime.parse("2021-11-19T00:00"), 1),
				Arguments.of(1, LocalDateTime.parse("2021-12-18T00:00"), LocalDateTime.parse("2021-12-19T00:00"), 0)
				);
	}
}
