package com.kti.restaurant.controller;

import com.kti.restaurant.dto.notification.CreateUpdateNotificationDto;
import com.kti.restaurant.mapper.NotificationMapper;
import com.kti.restaurant.model.Notification;
import com.kti.restaurant.service.contract.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "api/v1/notifications")
public class NotificationController {

    private INotificationService notificationService;
    private NotificationMapper notificationMapper;

    @Autowired
    NotificationController(INotificationService notificationService, NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    @GetMapping("")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return new ResponseEntity<>(notificationService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Integer id) {
        return new ResponseEntity<>(notificationService.findById(id), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Notification> createNotification(@RequestBody CreateUpdateNotificationDto notificationDto) throws Exception {
        Notification notification = notificationService.create(notificationMapper.fromCreateNotificationDtoToNotification(notificationDto));

        if(notification != null) {
            return new ResponseEntity<>(notification, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("")
    public ResponseEntity<Notification> updateNotification(@RequestBody CreateUpdateNotificationDto notificationDto) throws Exception {
        Notification notification = notificationService.update((notificationMapper.fromUpdateNotificationDtoToNotification(notificationDto)));

        if(notification != null) {
            return new ResponseEntity<>(notification, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<?> deleteNotification(@PathVariable Integer id) {
        notificationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
