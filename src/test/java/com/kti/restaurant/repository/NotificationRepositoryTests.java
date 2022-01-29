package com.kti.restaurant.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import com.kti.restaurant.model.Notification;

@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
public class NotificationRepositoryTests {

	@Autowired
	private NotificationRepository notificationRepository;
	
	@ParameterizedTest
	@MethodSource("provideParametersForFindAll")
	public void findAll(int expected) {
		List<Notification> notifications = notificationRepository.findAll();
		
		assertEquals(expected, notifications.size());
	}
	
	private static Stream<Arguments> provideParametersForFindAll() {
		
		return Stream.of(
				Arguments.of(6)
				);
	}
	
	
	@ParameterizedTest
	@MethodSource("provideParametersForFindNotificationsForWaiter")
	public void findNotificationsForWaiter(int expected, int waiterId) {
		List<Notification> notifications = notificationRepository.findNotificationsForWaiter(waiterId);
		
		assertEquals(expected, notifications.size());
	}
	
	private static Stream<Arguments> provideParametersForFindNotificationsForWaiter() {
		
		return Stream.of(
				Arguments.of(2, 8),
				Arguments.of(4, 7)
				);
	}
	
	@ParameterizedTest
	@MethodSource("provideParametersForFindNotificationsForBartenderAndCook")
	public void findNotificationsForWaiter(int expected) {
		List<Notification> notifications = notificationRepository.findNotificationsForCookAndBartender();
		
		assertEquals(expected, notifications.size());
	}
	
	private static Stream<Arguments> provideParametersForFindNotificationsForBartenderAndCook() {
		
		return Stream.of(
				Arguments.of(1)
				);
	}
	
}
