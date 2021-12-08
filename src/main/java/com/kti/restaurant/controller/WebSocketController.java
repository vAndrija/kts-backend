package com.kti.restaurant.controller;

import com.kti.restaurant.service.contract.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class WebSocketController {

    private INotificationService notificationService;

    @Autowired
    public WebSocketController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @MessageMapping("/send/message")
    public Map<String, String> broadcastNotification(String message) {
        return notificationService.broadcastNotification(message);
    }
}
