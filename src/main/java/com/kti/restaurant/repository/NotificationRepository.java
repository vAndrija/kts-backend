package com.kti.restaurant.repository;

import com.kti.restaurant.model.Notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

	@Query("select n from Notification n join fetch n.orderItem")
	List<Notification> findAll();
	
	@Query("select n from Notification n join OrderItem oi on n.orderItem.id = oi.id join Order o on oi.order.id = o.id join Waiter w on o.waiter.id = w.id"
			+ " where w.id = ?1")
	List<Notification> findNotificationsForWaiter(Integer waiterId);
	
	@Query("select n from Notification n where n.orderItem=null")
	List<Notification> findNotificationsForCookAndBartender();
}
