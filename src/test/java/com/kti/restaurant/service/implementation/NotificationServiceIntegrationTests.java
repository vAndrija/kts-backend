package com.kti.restaurant.service.implementation;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Notification;
import com.kti.restaurant.model.OrderItem;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Transactional
public class NotificationServiceIntegrationTests {

	@Autowired
	private NotificationService notificationService;
	
	private static final String message = "Notification with given id does not exist in the system.";
	
	@Test
	public void findAll_ValidNumberOfNotifications() {
		List<Notification> notifications = notificationService.findAll();
		
		assertEquals(6, notifications.size());
	}
	
	
	@Test
	public void findById_ValidId_ReturnsExistingNotification() {
		Notification notification = notificationService.findById(1);
		
		assertEquals(Integer.valueOf(1), notification.getId());
		assertEquals("Napravljena je nova porudzbina.", notification.getMessage());
	}
	
	@Test
	public void findById_InvalidId_ThrowsMissingEntityException() {
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			notificationService.findById(100);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	@Rollback
	public void create_ValidNotification_ReturnsCreatedNotification() throws Exception {
		Notification notification = notificationService.create(new Notification("Poruka", null));
		
		assertEquals("Poruka", notification.getMessage());
	}
	
	@Test
	@Rollback
	public void delete_ValidId() {
		notificationService.delete(3);
		Exception exception = assertThrows(MissingEntityException.class, () -> {
			notificationService.findById(2);
		});
		
		assertEquals(message, exception.getMessage());
	}
	
	@Test
	public void getNotificationForCookAndBartender_ReturnsValidNotifications() {
		List<Notification> notifications = notificationService.getNotificationsForCookAndBartender();
		
		assertEquals(1, notifications.size());
	}
	
	@ParameterizedTest
	@MethodSource("provideParametersForGetNotificationForWaiter")
	public void getNotificationForWaiter_ReturnsValidNotifications(int expected, int waiterId) {
		List<Notification> notifications = notificationService.getNotificationForWaiter(waiterId);
		
		assertEquals(expected, notifications.size());
	}
	
	private static Stream<Arguments> provideParametersForGetNotificationForWaiter() {
		
		return Stream.of(
				Arguments.of(2, 8),
				Arguments.of(4, 7)
				);
	}
	
	
}
