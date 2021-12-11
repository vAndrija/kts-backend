package com.kti.restaurant.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.notification.CreateUpdateNotificationDto;
import com.kti.restaurant.dto.notification.NotificationDto;
import com.kti.restaurant.model.Notification;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.NotificationService;
import com.kti.restaurant.service.implementation.OrderItemService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NotificationControllerIntegrationTests {
	 @Autowired
	    private TestRestTemplate restTemplate;

	    @Autowired
	    private NotificationService notificationService;
	    
	    @Autowired
	    private OrderItemService orderItemService;

	    private String accessToken;

	    private HttpHeaders headers;

	    @BeforeEach
	    public void login() {
	        ResponseEntity<UserTokenState> responseEntity = restTemplate.
	        		postForEntity("/api/v1/auth/login",
	                        new JwtAuthenticationRequest("kristinamisic@gmail.com", "123"),
	                        UserTokenState.class);
	        
	        accessToken = responseEntity.getBody().getAccessToken();
	        
	        headers = new HttpHeaders();
	        headers.add("Authorization", "Bearer " + accessToken);
	    }
	    
	    @Test
	    public void findAll_NotificationsExist_ReturnStatusOk() {
	    	 HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
	         ResponseEntity<NotificationDto[]> responseEntity = restTemplate
	        		 .exchange("/api/v1/notifications", HttpMethod.GET, httpEntity, NotificationDto[].class);

	         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	         assertEquals(7, List.of(responseEntity.getBody()).size());
	    }
	    
	    @Test
	    public void findById_ValidNotificationId_ReturnsStatusOk() {
	    	HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
	        ResponseEntity<NotificationDto> responseEntity = restTemplate
	        		.exchange("/api/v1/notifications/{id}", HttpMethod.GET, httpEntity, NotificationDto.class, 1);

	        NotificationDto notification = responseEntity.getBody();
	        
	        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	        assertEquals(Integer.valueOf(1), notification.getId());
	        assertEquals("Napravljena je nova porudzbina.", notification.getMessage());
	        assertEquals(Integer.valueOf(2), notification.getOrderItemDto().getId());
	    }
	    
	    @Test
	    public void findById_InvalidNotificationId_ReturnsNotFound() {
	        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
	        ResponseEntity<NotificationDto> responseEntity =
	                restTemplate.exchange("/api/v1/notifications/{id}", HttpMethod.GET, httpEntity, NotificationDto.class, 8);

	        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	    }
	    
	    @Test
	    public void createNotification_ValidNotificationDto_ReturnsStatusCreated() {
	    	int size = notificationService.findAll().size();
	    	HttpEntity<CreateUpdateNotificationDto> httpEntity = new HttpEntity<>(new CreateUpdateNotificationDto("Nova notifikacija", 1), headers);
	        ResponseEntity<NotificationDto> responseEntity = restTemplate
	        		.postForEntity("/api/v1/notifications", httpEntity, NotificationDto.class);

	        NotificationDto notification = responseEntity.getBody();
	        
	        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
	        assertEquals("Nova notifikacija", notification.getMessage());
	        assertEquals(1, notification.getOrderItemDto().getId());
	        assertEquals(size+1, notificationService.findAll().size());
	    

	        notificationService.delete(notification.getId());	
	        assertEquals(size, notificationService.findAll().size());
	    }
	    
	    @Test
	    public void createNotification_InvaliNotificationDto_ReturnsStatusBadRequest() {
	    	int size = notificationService.findAll().size();
	    	
	    	HttpEntity<CreateUpdateNotificationDto> httpEntity = new HttpEntity<>(new CreateUpdateNotificationDto("Nova porudzbina", null), headers);
	    	ResponseEntity<NotificationDto> responseEntity = restTemplate
	    			.postForEntity("/api/v1/notifications", httpEntity, NotificationDto.class);
	    
	    	assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	    	assertEquals(size, notificationService.findAll().size());
	    }

	    @Test
	    public void updateNotification_ValidNotificationId_ReturnsStatusOk() {
	    	HttpEntity<CreateUpdateNotificationDto> httpEntity = new HttpEntity<>(headers);
	    	
	    	ResponseEntity<NotificationDto> responseEntity = restTemplate
	    			.exchange("/api/v1/notifications/{id}", HttpMethod.PUT, httpEntity, NotificationDto.class, 1);
	    	
	    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	    	assertEquals(Boolean.TRUE, responseEntity.getBody().getSeen());
	    	//Posto notifikacija ne moze da bude pregledana pa nepregledana ne postoji funkcija za revertovanje
	    }
	    
	    @Test
	    public void updateNotification_InvalidNotificationId_ReturnsStatusNotFound() {
	    	HttpEntity<CreateUpdateNotificationDto> httpEntity = new HttpEntity<>(headers);
	    	
	    	ResponseEntity<NotificationDto> responseEntity = restTemplate
	    			.exchange("/api/v1/notifications/{id}", HttpMethod.PUT, httpEntity, NotificationDto.class, 8);
	    	
	    	assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	    }
	    
	    @Test
	    public void deleteNotification_ValidId_ReturnsStatusNoContent() throws Exception {
	    	OrderItem orderItem = orderItemService.findById(1);
	    	Notification notification = new Notification("Nova", orderItem, false);
	    	
	    	Notification createdNotification = notificationService.create(notification);
	    	
	    	int size = notificationService.findAll().size();

	    	HttpEntity<CreateUpdateNotificationDto> httpEntity = new HttpEntity<>(headers);
	    	
	    	ResponseEntity<NotificationDto> responseEntity = restTemplate
	    			.exchange("/api/v1/notifications/{id}", HttpMethod.DELETE, httpEntity, NotificationDto.class, createdNotification.getId());
	    	
	    	assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
	    	assertEquals(size - 1, notificationService.findAll().size());
	    }
	    
	    @Test
	    public void deleteNotification_InvalidId_ReturnsStatusNoContent() {
	    	int size = notificationService.findAll().size();

	    	HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
	    	
	    	ResponseEntity<Void> responseEntity = restTemplate
	    			.exchange("/api/v1/notifications/{id}", HttpMethod.DELETE, httpEntity, Void.class, 8);
	    	
	    	assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	    	assertEquals(size, notificationService.findAll().size());
	    }
}
