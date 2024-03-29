package com.kti.restaurant.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.Bartender;
import com.kti.restaurant.model.Cook;
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
    private OrderItemService orderItemService;
    private CookService cookService;
    private BartenderService bartenderService;
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    NotificationService(NotificationRepository notificationRepository, OrderItemService orderItemService, 
    		SimpMessagingTemplate simpMessagingTemplate, CookService cookService, BartenderService bartenderService) {
        this.notificationRepository = notificationRepository;
        this.orderItemService = orderItemService;
        this.cookService = cookService;
        this.bartenderService = bartenderService;
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
    public void delete(Integer id) {
    	for(Notification notification: this.findAll()) {
    		if(notification.getOrderItem().getId() == id) {
    			notificationRepository.deleteById(notification.getId());
    		}
    	}
    }

    @Override
    public Map<String, String> broadcastOrderItemStatusChanged(String message) {
        Map<String, String> messageConverted = parseMessage(message);

        if (messageConverted != null) {
            if (messageConverted.containsKey("status")) {
            	String status = messageConverted.get("status");
            	
            	if (status.equalsIgnoreCase("Pripremljeno") || status.equalsIgnoreCase("Preuzeto")) {
            		Integer orderItemId = Integer.parseInt(messageConverted.get("orderItemId"));
            		Integer waiterId = orderItemService.findByIdWithOrderAndWaiter(orderItemId).getOrder().getWaiter().getId();
            		
            		this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + waiterId, messageConverted);
            	}
            } 
        }

        return messageConverted;
    }
    
    @Override
    public Map<String, String> broadcastOrderCreated(String message) {
        Map<String, String> messageConverted = parseMessage(message);
        
        if (messageConverted != null) {
            List<Bartender> bartenders = bartenderService.findAll();
            List<Cook> cooks = cookService.findAll();
            
            bartenders.forEach(bartender -> this.notifyUser(bartender.getId(), messageConverted));
            cooks.forEach(cook -> this.notifyUser(cook.getId(), messageConverted));   
        }

        return messageConverted;
    }
    
    private void notifyUser(Integer userId, Map<String, String> messageConverted) {
    	this.simpMessagingTemplate.convertAndSend("/socket-publisher/" + userId, messageConverted);
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

	@Override
	public Notification update(Notification entity, Integer id) throws Exception {
		return null;
	}   
	
	@Override
	public List<Notification> getNotificationForWaiter(Integer id) {
		return notificationRepository.findNotificationsForWaiter(id);
	}
	
	@Override 
	public List<Notification> getNotificationsForCookAndBartender() {
		return notificationRepository.findNotificationsForCookAndBartender();
	}
}
