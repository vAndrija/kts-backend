package com.kti.restaurant.service.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Notification;
import com.kti.restaurant.model.OrderItem;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class NotificationServiceIntegrationTests {

	@Autowired
	private NotificationService notificationService;
	
	private static final String message = "Notification with given id does not exist in the system.";
	
	@Test
	public void findAll_ValidNumberOfNotifications() {
		List<Notification> notifications = notificationService.findAll();
		
		assertEquals(notifications.size(), 7);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void findById_ValidId_ReturnsValidNotification() {
		Notification notification = notificationService.findById(1);
		
		assertEquals(notification.getId(),new Integer(1));
		assertEquals(notification.getMessage(), "Napravljena je nova porudzbina.");
		assertEquals(notification.getSeen(), true);
	}
	
	@Test
	public void findById_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			notificationService.findById(100);
		});
		
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@Rollback
	public void create_ValidNotification_ReturnsValidNotification() throws Exception {
		Notification notification = notificationService.create(new Notification("Poruka", new OrderItem(), false));
		
		assertEquals(notification.getMessage(), "Poruka");
		assertEquals(notification.getSeen(), false);
	}
	
	@Test
	@Rollback
	public void update_ValidId_ReturnsValidNotification() throws Exception {
		Notification notification = new Notification("Nova poruka", new OrderItem(), false);
		notification.setId(1);
		
		Notification notificationToUpdate = notificationService.update(notification, 1); 
		
		//Posto mozemo samo da update seen samo je to testirano.
		assertEquals(notificationToUpdate.getSeen(), false);
	}
	
	@Test
	public void update_InvalidId_ThrowsMissingEntityException() {		
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			notificationService.update(null, 10);
		});
		
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	@Rollback
	public void delete_ValidId() {
		notificationService.delete(1);
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			notificationService.findById(1);
		});
		
		assertEquals(exception.getMessage(), message);
	}
	
	@Test
	public void delete_InvalidId() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			notificationService.delete(10);
		});
		
		assertEquals(exception.getMessage(), message);
	}
	
	
}
