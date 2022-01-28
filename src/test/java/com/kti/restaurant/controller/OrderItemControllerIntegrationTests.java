package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.orderitem.CreateOrderItemDto;
import com.kti.restaurant.dto.orderitem.OrderItemDto;
import com.kti.restaurant.dto.orderitem.UpdateOrderItemDto;
import com.kti.restaurant.model.OrderItem;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.model.enums.OrderItemStatus;
import com.kti.restaurant.service.implementation.MenuItemService;
import com.kti.restaurant.service.implementation.OrderItemService;
import com.kti.restaurant.service.implementation.OrderService;
import com.kti.restaurant.utils.RestResponsePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class OrderItemControllerIntegrationTests {

    private static final String URL_PREFIX = "/api/v1/order-items";

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;

    private HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void login() {
        ResponseEntity<UserTokenState> responseEntity = restTemplate.postForEntity("/api/v1/auth/login",
                new JwtAuthenticationRequest("anapopovic@gmail.com", "123"), UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        headers.add("Authorization", "Bearer " + accessToken);

    }

    @Test
    public void create_ValidOrderItem_ReturnsCreated() throws Exception {
        int size = orderItemService.findAll().size();

        HttpEntity<CreateOrderItemDto> httpEntity = new HttpEntity<>(new CreateOrderItemDto(2, "sa sojinim mlekom",
                "U pripremi", 1, 3, 1), headers);
        ResponseEntity<OrderItemDto> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, OrderItemDto.class);

        OrderItemDto orderItemDto = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        List<OrderItem> orderItemList = orderItemService.findAll();
        assertEquals(size + 1, orderItemList.size());

        assertEquals(2, orderItemDto.getQuantity());
        assertEquals("sa sojinim mlekom", orderItemDto.getNote());
        assertEquals(OrderItemStatus.PREPARATION.getType(), orderItemDto.getStatus());
        assertEquals(1, orderItemDto.getPriority());
        assertEquals(1, orderItemDto.getOrderId());
        assertEquals(3, orderItemDto.getMenuItemId());
        orderItemService.delete(orderItemDto.getId());

    }

    @Test
    public void createInvalidOrderItem_InvalidQuantity_ReturnsBadRequest() {
        int size = orderItemService.findAll().size();

        HttpEntity<CreateOrderItemDto> httpEntity = new HttpEntity<>(new CreateOrderItemDto(0, "sa sojinim mlekom",
                "U pripremi", 1, 3, 1), headers);
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(size, orderItemService.findAll().size());

    }

    @Test
    public void createInvalidOrderItem_InvalidPriority_ReturnsBadRequest() {
        int size = orderItemService.findAll().size();

        HttpEntity<CreateOrderItemDto> httpEntity = new HttpEntity<>(new CreateOrderItemDto(1, "sa sojinim mlekom",
                "U pripremi", 0, 3, 1), headers);
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(size, orderItemService.findAll().size());

    }

    @Test
    public void createInvalidOrderItem_InvalidMenuItem_ReturnsBadRequest() {
        int size = orderItemService.findAll().size();

        HttpEntity<CreateOrderItemDto> httpEntity = new HttpEntity<>(new CreateOrderItemDto(1, "sa sojinim mlekom",
                "U pripremi", 1, null, 1), headers);
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(size, orderItemService.findAll().size());

    }

    @Test
    public void createInvalidOrderItem_InvalidOrder_ReturnsBadRequest() {
        int size = orderItemService.findAll().size();

        HttpEntity<CreateOrderItemDto> httpEntity = new HttpEntity<>(new CreateOrderItemDto(1, "sa sojinim mlekom",
                "U pripremi", 1, 3, null), headers);
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(size, orderItemService.findAll().size());

    }

    @Test
    public void getOrderItems_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderItemDto[]> responseEntity = restTemplate.exchange(URL_PREFIX, HttpMethod.GET,
                httpEntity, OrderItemDto[].class);

        OrderItemDto[] orderItemDtos = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(11, orderItemDtos.length);
        assertEquals(1, orderItemDtos[0].getPriority());
        assertEquals(2, orderItemDtos[0].getQuantity());
        assertEquals(2, orderItemDtos[1].getPriority());
        assertEquals(1, orderItemDtos[1].getQuantity());

    }

    @Test
    public void getUnacceptedOrderItems_ReturnsOk() {
    	
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("urosmatic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        
        ParameterizedTypeReference<RestResponsePage<OrderItemDto>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<RestResponsePage<OrderItemDto>> entity = restTemplate.exchange(URL_PREFIX + "/unaccepted?page={page}&size={size}", HttpMethod.GET,
                httpEntity, responseType, 0, 8);

        List<OrderItemDto> orderItemDtos = entity.getBody().getContent();

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(3, orderItemDtos.size());
        assertEquals(1, orderItemDtos.get(0).getPriority());
        assertEquals(2, orderItemDtos.get(0).getQuantity());
        assertEquals(2, orderItemDtos.get(1).getPriority());
        assertEquals(1, orderItemDtos.get(1).getQuantity());
    }
    
    @Test
    public void getOrderItem_ValidId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderItemDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, OrderItemDto.class, 1);

        OrderItemDto orderItemDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, orderItemDto.getPriority());
        assertEquals(2, orderItemDto.getQuantity());
        assertEquals(OrderItemStatus.ORDERED.getType(), orderItemDto.getStatus());
        assertEquals(1, orderItemDto.getMenuItemId());
        assertEquals(1, orderItemDto.getOrderId());

    }

    @Test
    public void getOrderItem_InvalidId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderItemDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, OrderItemDto.class, 8963);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void delete_ValidId_ReturnsNoContent() throws Exception {
        OrderItem orderItem = orderItemService.create(new OrderItem(2, "sa sojinim mlekom",
                OrderItemStatus.PREPARATION, 1, menuItemService.findById(3), orderService.findById(3)));

        int size = orderItemService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderItemDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE,
                httpEntity, OrderItemDto.class, orderItem.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(size - 1, orderItemService.findAll().size());

    }

    @Test
    public void delete_InvalidId_ReturnsNotFound() {
        int size = orderItemService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderItemDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE,
                httpEntity, OrderItemDto.class, 58963);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, orderItemService.findAll().size());

    }

    @Test
    public void update_ValidId_ReturnsOk() throws Exception {
        HttpEntity<UpdateOrderItemDto> httpEntity = new HttpEntity<>(new UpdateOrderItemDto(2, "sa sojinim mlekom",
                OrderItemStatus.PREPARED, 1, 3, 2, 2, null), headers);

        ResponseEntity<OrderItemDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT,
                httpEntity, OrderItemDto.class, 4);

        OrderItemDto orderItemDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, orderItemDto.getQuantity());
        assertEquals("sa sojinim mlekom", orderItemDto.getNote());
        assertEquals(OrderItemStatus.PREPARED.getType(), orderItemDto.getStatus());
        assertEquals(1, orderItemDto.getPriority());
        assertEquals(3, orderItemDto.getMenuItemId());
        assertEquals(2, orderItemDto.getBartenderId());

        OrderItem orderItem = orderItemService.findById(4);
        assertEquals(2, orderItem.getQuantity());
        assertEquals("sa sojinim mlekom", orderItem.getNote());
        assertEquals(OrderItemStatus.PREPARED, orderItem.getStatus());
        assertEquals(1, orderItem.getPriority());
        assertEquals(3, orderItem.getMenuItem().getId());
        assertEquals(2, orderItem.getBartender().getId());

        orderItem.setQuantity(1);
        orderItem.setNote("");
        orderItem.setStatus(OrderItemStatus.PREPARATION);
        orderItemService.update(orderItem, 4);

    }

    @Test
    public void update_InvalidId_ReturnsNotFound() {
        HttpEntity<UpdateOrderItemDto> httpEntity = new HttpEntity<>(new UpdateOrderItemDto(3, "sa sojinim mlekom",
                OrderItemStatus.PREPARATION, 1, 3, 2, 2, null), headers);

        ResponseEntity<OrderItemDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT,
                httpEntity, OrderItemDto.class, 300);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void update_ValidStatus_ReturnsOk() throws Exception {
        HttpEntity<?> httpEntity = new HttpEntity<>("Pripremljeno", headers);

        ResponseEntity<OrderItemDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/status/{id}",
                HttpMethod.POST, httpEntity, OrderItemDto.class, 4);


        OrderItemDto orderItemDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(OrderItemStatus.PREPARED.getType(), orderItemDto.getStatus());

        OrderItem orderItem = orderItemService.findById(4);
        assertEquals(OrderItemStatus.PREPARED, orderItem.getStatus());

        orderItem.setStatus(OrderItemStatus.PREPARATION);
        orderItemService.update(orderItem, 4);

    }


    @Test
    public void update_InvalidStatus_ReturnsBadRequest() {
        HttpEntity<?> httpEntity = new HttpEntity<>(" ", headers);

        ResponseEntity<OrderItemDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/status/{id}",
                HttpMethod.POST, httpEntity, OrderItemDto.class, 4);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    public void getOrderItemsForEmployee_ValidId_ReturnsOk() {
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("lukaperic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ParameterizedTypeReference<RestResponsePage<OrderItemDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<RestResponsePage<OrderItemDto>> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/employee/{id}?page={page}&size={size}", HttpMethod.GET,
                httpEntity, responseType, 2, 0, 8);

        List<OrderItemDto> orderItems = responseEntity1.getBody().getContent();

        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals(2, orderItems.size());

    }

    @Test
    public void getOrderItemsForEmployee_InvalidId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(URL_PREFIX + "/employee/{id}?page={page}&size={size}", HttpMethod.GET,
                httpEntity, Object.class, 300, 0, 8);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void getOrderItemsForOrder_ValidId_ReturnsOk() {
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("jovanpetrovic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<OrderItemDto[]> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/order/{id}", HttpMethod.GET,
                httpEntity, OrderItemDto[].class, 1);

        OrderItemDto[] orderItems = responseEntity1.getBody();

        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals(9, orderItems.length);

    }

    @Test
    public void getOrderItemsForEmployeeAndStatus_ValidId_ReturnsOk() {
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("lukaperic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ParameterizedTypeReference<RestResponsePage<OrderItemDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<RestResponsePage<OrderItemDto>> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/filter/{id}/{status}?page={page}&size={size}", HttpMethod.GET,
                httpEntity, responseType, 4, "U pripremi", 0, 8);

        List<OrderItemDto> orderItems = responseEntity1.getBody().getContent();

        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals(2, orderItems.size());

    }

    @Test
    public void getOrderItemsForEmployeeAndStatus_InvalidId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(URL_PREFIX + "/filter/{id}/{status}?page={page}&size={size}", HttpMethod.GET,
                httpEntity, Object.class, 300, "U pripremi", 0, 8);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void getOrderItemsForEmployeeAndStatus_InvalidStatus_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<Object> responseEntity = restTemplate.exchange(URL_PREFIX + "/filter/{id}/{status}?page={page}&size={size}", HttpMethod.GET,
                httpEntity, Object.class, 4, " ", 0, 8);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }


}
