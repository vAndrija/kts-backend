package com.kti.restaurant.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.repository.RestaurantTableRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantTableServiceUnitTests {

	@InjectMocks
	private RestaurantTableService tableService;
	
	@Mock
	private RestaurantTableRepository tableRepository;
	
	private static final String message = "Restaurant with given id does not exist in the system.";
	
	@BeforeEach
	public void setup() {
		RestaurantTable table = new RestaurantTable(false, 1, 4, 1, 1);
		table.setId(1);
		
		when(tableRepository.findById(1)).thenReturn(Optional.of(table));
	}
	
	@Test
	public void findById_ValidId_ReturnsValidRestaurantTable() throws Exception {
		RestaurantTable table = tableService.findById(1);
		
		assertEquals(Integer.valueOf(1), table.getId());
		assertEquals(Integer.valueOf(4), table.getCapacity());
		assertEquals(Integer.valueOf(1), table.getxCoordinate());
		assertEquals(Integer.valueOf(1), table.getyCoordinate());
	}
	
	@Test
	public void findById_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			tableService.findById(2);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	public void update_ValidId_ReturnsUpdatesRestaurantTable() throws Exception {
		RestaurantTable tableToUpdate = new RestaurantTable(false, 1, 5, 1, 1);
		tableToUpdate.setId(1);
		
		when(tableRepository.save(any())).thenReturn(tableToUpdate);
		
		RestaurantTable table = tableService.update(tableToUpdate, 1);
		
		assertEquals(tableToUpdate.getId(), table.getId());
		assertEquals(tableToUpdate.getCapacity(), table.getCapacity());
		assertEquals(tableToUpdate.getxCoordinate(), table.getxCoordinate());
		assertEquals(tableToUpdate.getyCoordinate(), table.getyCoordinate());
	}
	
	@Test
	public void update_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			tableService.update(null, 2);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	public void delete_ValidId() throws Exception {
		tableService.delete(1);
		verify(tableRepository, times(1)).findById(1);
		verify(tableRepository, times(1)).deleteById(1);
	}
	
	@Test
	public void delete_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			tableService.delete(2);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
}
