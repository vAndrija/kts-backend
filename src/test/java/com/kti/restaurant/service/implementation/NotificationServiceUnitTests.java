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
import com.kti.restaurant.model.Notification;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.repository.NotificationRepository;

@SpringBootTest
public class NotificationServiceUnitTests {
	
	@InjectMocks
	private NotificationService notificationService;
	
	@Mock
	private NotificationRepository notificationRepository;
	
	private static final String message = "Notification with given id does not exist in the system.";
	
	@BeforeEach
	public void setup() {
		Notification notification = new Notification("Poruka", new OrderItem(), false);
		notification.setId(1);
		
		when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));
	}
	
	@Test
	public void findById_ValidId_ExistsNotification() {
		Notification notification = notificationService.findById(1);
		
		assertEquals("Poruka", notification.getMessage());
	}
	
	@Test
	public void findById_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
            notificationService.findById(2);
        });
		
		assertEquals(message, exception.getMessage());
	}
		
	@Test
	public void update_ValidId_NotificationUpdated() throws Exception {
		Notification notificationToUpdate = new Notification("Poruka", new OrderItem(), true);
		notificationToUpdate.setId(1);
		
		when(notificationRepository.save(any())).thenReturn(notificationToUpdate);
		
		Notification notification = notificationService.update(notificationToUpdate, 1);
		
		assertEquals(notificationToUpdate.getSeen(), notification.getSeen());
		assertEquals(notificationToUpdate.getMessage(), notification.getMessage());
		
	}
	
	@Test
	public void update_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			notificationService.update(null, 2);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	public void delete_ValidId_NotificationDeleted() {
		notificationService.delete(1);
		
		Notification notification = new Notification("Poruka", new OrderItem(), false);
		notification.setId(1);
		
		verify(notificationRepository, times(1)).findById(1);
	} 
	
	@Test
	public void delete_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			notificationService.delete(2);
		});
		
		assertEquals(message, exception.getMessage());	
	}
}
