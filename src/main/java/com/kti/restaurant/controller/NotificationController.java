package com.kti.restaurant.controller;

import com.kti.restaurant.dto.notification.CreateUpdateNotificationDto;
import com.kti.restaurant.dto.notification.NotificationDto;
import com.kti.restaurant.mapper.NotificationMapper;
import com.kti.restaurant.model.Notification;
import com.kti.restaurant.service.contract.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER')")
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        List<NotificationDto> notificationDtos = notificationService.findAll().stream()
                .map(notification -> this.notificationMapper.fromNotificationToNotificationDto(notification)).collect(Collectors.toList());

        return new ResponseEntity<>(notificationDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER')")
    public ResponseEntity<NotificationDto> getNotificationById(@PathVariable Integer id) throws Exception {
        Notification notification = notificationService.findById(id);

        return new ResponseEntity<>(notificationMapper.fromNotificationToNotificationDto(notification), HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER')")
    public ResponseEntity<NotificationDto> createNotification(@Valid @RequestBody CreateUpdateNotificationDto notificationDto) throws Exception {
        Notification notification = notificationService.create(notificationMapper.fromCreateNotificationDtoToNotification(notificationDto));

        if(notification != null) {
            return new ResponseEntity<>(notificationMapper.fromNotificationToNotificationDto(notification), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER')")
    public ResponseEntity<NotificationDto> updateNotification(@PathVariable Integer id) throws Exception {
        Notification notification = notificationService.update(new Notification() {{ setSeen(Boolean.TRUE); }}, id);
        
        return new ResponseEntity<>(notificationMapper.fromNotificationToNotificationDto(notification), HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    @PreAuthorize("hasAnyRole('COOK', 'BARTENDER', 'WAITER')")
    public ResponseEntity<?> deleteNotification(@PathVariable Integer id) throws Exception {
        notificationService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
