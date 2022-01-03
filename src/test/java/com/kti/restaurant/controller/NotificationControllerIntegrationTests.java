package com.kti.restaurant.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
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
	    public void findAll_ReturnOk() {
	    	 HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
	         ResponseEntity<NotificationDto[]> responseEntity = restTemplate
	        		 .exchange("/api/v1/notifications", HttpMethod.GET, httpEntity, NotificationDto[].class);

	         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	         assertEquals(7, List.of(responseEntity.getBody()).size());
	    }
	    
	    @Test
	    public void findById_ValidNotificationId_ReturnsOk() {
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
	    public void createNotification_ValidNotificationDto_ReturnsCreated() {
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
	    
	    @ParameterizedTest
		@MethodSource("provideCreateNotificiationDto")
	    public void createNotification_InvaliNotificationDto_ReturnsBadRequest(String message, Integer orderItemId, String bodyParameter, String errorMessage) {
	    	int size = notificationService.findAll().size();
	    	
	    	HttpEntity<CreateUpdateNotificationDto> httpEntity = new HttpEntity<>(new CreateUpdateNotificationDto(message, orderItemId), headers);
	    	
	    	ParameterizedTypeReference<HashMap<String, String>> responseType = new ParameterizedTypeReference<HashMap<String, String>>() { };
			
			ResponseEntity<HashMap<String, String>> responseEntity = restTemplate
	    			.exchange("/api/v1/notifications",HttpMethod.POST, httpEntity, responseType);
	    
	    	HashMap<String, String> body = responseEntity.getBody();
	    	
	    	assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	    	assertEquals(size, notificationService.findAll().size());
	    	assertEquals(errorMessage, body.get(bodyParameter));
	    }

	    private static Stream<Arguments> provideCreateNotificiationDto() {
			
			return Stream.of(
					Arguments.of(null, 1, "message", "Message should not be null or empty."),
					Arguments.of("Nesto", null, "orderItemId", "Order Item id should not be null or empty.")
					);
		}
		
	    
	    @Test
	    public void updateNotification_ValidNotificationId_ReturnsOk() {
	    	HttpEntity<CreateUpdateNotificationDto> httpEntity = new HttpEntity<>(headers);
	    	
	    	ResponseEntity<NotificationDto> responseEntity = restTemplate
	    			.exchange("/api/v1/notifications/{id}", HttpMethod.PUT, httpEntity, NotificationDto.class, 1);
	    	
	    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	    	assertEquals(Boolean.TRUE, responseEntity.getBody().getSeen());
	    	//Posto notifikacija ne moze da bude pregledana pa nepregledana ne postoji funkcija za revertovanje
	    }
	    
	    @Test
	    public void updateNotification_InvalidNotificationId_ReturnsNotFound() {
	    	HttpEntity<CreateUpdateNotificationDto> httpEntity = new HttpEntity<>(headers);
	    	
	    	ResponseEntity<NotificationDto> responseEntity = restTemplate
	    			.exchange("/api/v1/notifications/{id}", HttpMethod.PUT, httpEntity, NotificationDto.class, 8);
	    	
	    	assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	    }
	    
	    @Test
	    public void deleteNotification_ValidId_ReturnsNoContent() throws Exception {
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
	    public void deleteNotification_InvalidId_ReturnsNoContent() {
	    	int size = notificationService.findAll().size();

	    	HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
	    	
	    	ResponseEntity<Void> responseEntity = restTemplate
	    			.exchange("/api/v1/notifications/{id}", HttpMethod.DELETE, httpEntity, Void.class, 8);
	    	
	    	assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	    	assertEquals(size, notificationService.findAll().size());
	    }
}
