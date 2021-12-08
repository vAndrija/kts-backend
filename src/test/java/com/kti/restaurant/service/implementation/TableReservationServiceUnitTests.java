package com.kti.restaurant.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.model.TableReservation;
import com.kti.restaurant.repository.TableReservationRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class TableReservationServiceUnitTests {

	@InjectMocks
	private TableReservationService reservationService;
	
	@Mock
	private TableReservationRepository reservationRepository;
	
	private static final String message = "Table reservation with given id does not exist in the system.";
	
	@BeforeEach
	private void setup() {
		TableReservation reservation = new TableReservation("Ime", LocalDateTime.parse("2021-10-10T22:22"), new RestaurantTable());
		reservation.setId(1);
		
		when(reservationRepository.findById(1)).thenReturn(Optional.of(reservation));
	}
	
	@Test
	public void findById_ValidId_ReturnsValidTableReservation() throws Exception {
		TableReservation reservation = reservationService.findById(1);
		
		assertEquals(reservation.getId(), Integer.valueOf(1));
		assertEquals(reservation.getName(), "Ime");
		assertEquals(reservation.getDurationStart(), LocalDateTime.parse("2021-10-10T22:22"));
	}
	
	@Test
	public void findById_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reservationService.findById(10);
		});
		
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	public void update_ValidId_ReturnsUpdatedTableReservation() throws Exception {
		TableReservation reservationToUpdate = new TableReservation("NovoIme", LocalDateTime.parse("2021-10-10T22:22"), new RestaurantTable());
		reservationToUpdate.setId(1);
		
		when(reservationRepository.save(any())).thenReturn(reservationToUpdate);

		TableReservation reservation = reservationService.update(reservationToUpdate, 1);
		assertEquals(reservationToUpdate.getId(), reservation.getId());
		assertEquals(reservationToUpdate.getDurationStart(), reservation.getDurationStart());
	}
	
	@Test
	public void update_InvalidId_ThrowsMissingEntityException() {
		when(reservationRepository.save(any())).thenReturn(Optional.empty());
		
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reservationService.update(null, 10);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	public void delete_ValidId() throws Exception {
		reservationService.delete(1);
		verify(reservationRepository, times(1)).findById(1);
		verify(reservationRepository, times(1)).deleteById(1);
	}
	
	@Test
	public void delete_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			reservationService.delete(10);
		});
		
		assertEquals(message, exception.getMessage());
	}
}
