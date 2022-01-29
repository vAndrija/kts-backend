package com.kti.restaurant.service.contract;

import com.kti.restaurant.model.Notification;

import java.util.List;
import java.util.Map;

public interface INotificationService extends IService<Notification>{
    Map<String, String> broadcastOrderItemStatusChanged(String message);
    Map<String, String> broadcastOrderCreated(String message);
	List<Notification> getNotificationForWaiter(Integer id);
	List<Notification> getNotificationsForCookAndBartender();
}
