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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;


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
        HttpEntity<CreateOrderItemDto> httpEntity = new HttpEntity<>(new CreateOrderItemDto(2, "sa sojinim mlekom",
                OrderItemStatus.PREPARATION, 1, 3, 1), headers);
        ResponseEntity<OrderItemDto> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, OrderItemDto.class);

        OrderItemDto orderItemDto = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(2, orderItemDto.getQuantity());
        assertEquals("sa sojinim mlekom", orderItemDto.getNote());
        assertEquals(OrderItemStatus.PREPARATION, orderItemDto.getStatus());
        assertEquals(1, orderItemDto.getPriority());
        assertEquals(1, orderItemDto.getOrderId());
        assertEquals(3, orderItemDto.getMenuItemId());
        orderItemService.delete(orderItemDto.getId());

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
    public void getOrderItem_ValidId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderItemDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, OrderItemDto.class, 1);

        OrderItemDto orderItemDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, orderItemDto.getPriority());
        assertEquals(2, orderItemDto.getQuantity());
        assertEquals(OrderItemStatus.ORDERED, orderItemDto.getStatus());
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
        assertEquals(OrderItemStatus.PREPARED, orderItemDto.getStatus());
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
    public void update_InvalidStatus_ReturnsBadRequest() {
        HttpEntity<UpdateOrderItemDto> httpEntity = new HttpEntity<>(new UpdateOrderItemDto(3, "sa sojinim mlekom",
                OrderItemStatus.PREPARATION, 1, 3, 2, 2, null), headers);

        ResponseEntity<OrderItemDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT,
                httpEntity, OrderItemDto.class, 4);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


}
