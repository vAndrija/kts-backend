package com.kti.restaurant.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.model.TableReservation;


@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class TableReservationServiceIntegrationTests {
	
	@Autowired
	private TableReservationService reservationService;
	
	private static final String message = "Table reservation with given id does not exist in the system.";
	
	@Test
	public void findAll_ReturnsValidTableReservations() {
		List<TableReservation> reservations = reservationService.findAll();
		
		assertEquals(2, reservations.size());
	}
	
	@Test
	public void findById_ValidId_ReturnsValidTableReservation() throws Exception {
		TableReservation reservation = reservationService.findById(1);
		
		assertEquals(Integer.valueOf(1), reservation.getId());
		assertEquals("Milica Petric", reservation.getName() );
		assertEquals(LocalDateTime.parse("2021-11-18T16:00"), reservation.getDurationStart());
	}
	
	@Test
	public void findById_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reservationService.findById(10);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	@Rollback
	public void create_ValidTableReservation_ReturnsValidTableReservation() throws Exception {
		TableReservation reservation = reservationService.create(new TableReservation("Ime", LocalDateTime.parse("2021-12-12T18:00"), new RestaurantTable()));
		
		assertEquals(Integer.valueOf(3), reservation.getId());
		assertEquals("Ime", reservation.getName());
		assertEquals(LocalDateTime.parse("2021-12-12T18:00"), reservation.getDurationStart());
	}
	
	@Test
	@Rollback
	public void update_ValidId_ReturnsUpdatedTableReservation() throws Exception {
		RestaurantTable table = new RestaurantTable(false, 1, 4, 0, 1);
		table.setId(1);
		
		TableReservation reservationToUpdate = new TableReservation("NovoIme", LocalDateTime.parse("2021-10-10T22:22"), table);
		reservationToUpdate.setId(1);

		TableReservation reservation = reservationService.update(reservationToUpdate, 1);
		assertEquals(reservationToUpdate.getDurationStart(), reservation.getDurationStart());
		assertEquals(table.getId(), reservation.getTable().getId());
	}
	
	@Test
	public void update_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reservationService.update(null, 10);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	@Rollback
	public void delete_ValidId() throws Exception {
		reservationService.delete(1);
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reservationService.findById(1);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	public void delete_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reservationService.delete(10);
		});
		
		assertEquals(message, exception.getMessage());
	}
}
