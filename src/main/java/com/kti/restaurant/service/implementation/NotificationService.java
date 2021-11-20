package com.kti.restaurant.service.implementation;

import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Notification;
import com.kti.restaurant.repository.NotificationRepository;
import com.kti.restaurant.service.contract.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService implements INotificationService {
    private NotificationRepository notificationRepository;

    @Autowired
    NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> findAll() {
         return notificationRepository.findAll();
    }

    @Override
    public Notification findById(Integer id) {
        Notification notification = notificationRepository.findById(id).orElse(null);

        if (notification == null) {
             throw new MissingEntityException("Notification with given id does not exist in the system.");
        }

        return notification;
    }

    @Override
    public Notification create(Notification notification) throws Exception {
        return notificationRepository.save(notification);
    }

    @Override
    public Notification update(Notification notification, Integer id) throws Exception {
        Notification notificationToUpdate = this.findById(id);

        notificationToUpdate.setSeen(notification.getSeen());

        notificationRepository.save(notificationToUpdate);

        return notificationToUpdate;
    }

    @Override
    public void delete(Integer id) {
        this.findById(id);
        notificationRepository.deleteById(id);
    }
}
