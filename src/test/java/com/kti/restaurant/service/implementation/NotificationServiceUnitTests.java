package com.kti.restaurant.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotificationServiceUnitTests {
	
	@InjectMocks
	private NotificationService notificationService;
	
	@Mock
	private NotificationRepository notificationRepository;
	
	private static final String message = "Notification with given id does not exist in the system.";
	
	@BeforeEach
	public void setup() {
		OrderItem oi = new OrderItem();
		oi.setId(1);
		
		Notification notification = new Notification("Poruka", oi);
		notification.setId(1);
		
		List<Notification> notifications = new ArrayList<Notification>();
		notifications.add(notification);
		
		when(notificationRepository.findById(1)).thenReturn(Optional.of(notification));
		when(notificationRepository.findAll()).thenReturn(notifications);
	}
	
	@Test
	public void findById_ValidId_ReturnsExistsNotification() {
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
	public void delete_ValidId() {
		notificationService.delete(1);
		
		Notification notification = new Notification("Poruka", new OrderItem());
		notification.setId(1);
		
		verify(notificationRepository, times(1)).findAll();
	} 

}
