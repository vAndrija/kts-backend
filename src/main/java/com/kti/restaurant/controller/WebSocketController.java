package com.kti.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kti.restaurant.service.contract.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.HtmlUtils;
import org.springframework.stereotype.Controller;

import java.io.IOException;
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
