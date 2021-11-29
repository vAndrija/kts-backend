package com.kti.restaurant.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Notification;
import com.kti.restaurant.repository.NotificationRepository;
import com.kti.restaurant.service.contract.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService implements INotificationService {
    private NotificationRepository notificationRepository;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    NotificationService(NotificationRepository notificationRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.notificationRepository = notificationRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
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

    /* Poruka ce biti poslata svim klijentima koji su pretplatili na /socket-publisher topic,
     * a poruka koja im se salje je messageConverted (simpMessagingTemplate.convertAndSend metoda).
     * Na ovaj endpoint klijenti salju poruke, ruta na koju klijenti salju poruke je /send/message (parametar @MessageMapping anotacije)
     */
    @Override
    public Map<String, String> broadcastNotification(String message) {
        Map<String, String> messageConverted = parseMessage(message);

        if (messageConverted != null) {
            if (messageConverted.containsKey("toId") && messageConverted.get("toId") != null
                    && !messageConverted.get("toId").equals("")) {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + messageConverted.get("toId"),
                        messageConverted);
                this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + messageConverted.get("fromId"),
                        messageConverted);
            } else {
                this.simpMessagingTemplate.convertAndSend("/socket-publisher", messageConverted);
            }
        }

        return messageConverted;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> parseMessage(String message) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> parsedMessage;

        try {
            parsedMessage = mapper.readValue(message, Map.class);
        } catch (IOException e) {
            parsedMessage = null;
        }

        return parsedMessage;
    }
}
