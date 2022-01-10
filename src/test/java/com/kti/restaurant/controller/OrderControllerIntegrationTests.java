package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.order.CreateOrderDto;
import com.kti.restaurant.dto.order.OrderDto;
import com.kti.restaurant.dto.order.UpdateOrderDto;
import com.kti.restaurant.dto.orderitem.OrderItemDto;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.model.enums.OrderStatus;
import com.kti.restaurant.service.implementation.OrderService;
import com.kti.restaurant.service.implementation.RestaurantTableService;
import com.kti.restaurant.utils.RestResponsePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class OrderControllerIntegrationTests {

    private static final String URL_PREFIX = "/api/v1/orders";

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantTableService restaurantTableService;

    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;

    private HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void login() {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("anapopovic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        headers.add("Authorization", "Bearer " + accessToken);

    }

    @Test
    public void create_ValidOrder_ReturnsCreated() throws Exception {
        int size = orderService.findAll().size();

        HttpEntity<CreateOrderDto> httpEntity = new HttpEntity<>(new CreateOrderDto("Poručeno",
                LocalDateTime.parse("2021-10-10T14:52"), 500.00, 1, 7), headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, OrderDto.class);

        OrderDto orderDto = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        List<Order> orderList = orderService.findAll();
        assertEquals(size + 1, orderList.size());
        assertEquals(OrderStatus.ORDERED, OrderStatus.findType(orderDto.getStatus()));
        assertEquals(LocalDateTime.parse("2021-10-10T14:52"), orderDto.getDateOfOrder());
        assertEquals(500.00, orderDto.getPrice());
        assertEquals(1, orderDto.getTableId());

        orderService.delete(orderDto.getId());

    }

    @Test
    public void createInvalidOrder_InvalidTable_ReturnsBadRequest() {
        int size = orderService.findAll().size();

        HttpEntity<CreateOrderDto> httpEntity = new HttpEntity<>(new CreateOrderDto("Poručeno",
                LocalDateTime.parse("2021-10-10T14:52"), 500.00, null, 7), headers);
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(size, orderService.findAll().size());

    }

    @Test
    public void createInvalidOrder_InvalidWaiter_ReturnsBadRequest() {
        int size = orderService.findAll().size();

        HttpEntity<CreateOrderDto> httpEntity = new HttpEntity<>(new CreateOrderDto("Poručeno",
                LocalDateTime.parse("2021-10-10T14:52"), 500.00, 1, null), headers);
        ResponseEntity<Object> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(size, orderService.findAll().size());

    }

    @Test
    public void getOrders_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderDto[]> responseEntity = restTemplate.exchange(URL_PREFIX, HttpMethod.GET,
                httpEntity, OrderDto[].class);

        OrderDto[] orderDtoList = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(3, orderDtoList.length);
        assertEquals(OrderStatus.ORDERED, OrderStatus.findType(orderDtoList[0].getStatus()));
        assertEquals(OrderStatus.PAID, OrderStatus.findType(orderDtoList[1].getStatus()));
        assertEquals(1520, orderDtoList[0].getPrice());
        assertEquals(1830, orderDtoList[1].getPrice());

    }

    @Test
    public void getOrder_ValidId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, OrderDto.class, 1);

        OrderDto orderDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(OrderStatus.ORDERED, OrderStatus.findType(orderDto.getStatus()));
        assertEquals(LocalDateTime.parse("2021-11-19T14:15"), orderDto.getDateOfOrder());
        assertEquals(1520, orderDto.getPrice());
        assertEquals(3, orderDto.getTableId());
        assertEquals(7, orderDto.getWaiterId());

    }

    @Test
    public void getOrder_InvalidId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, OrderDto.class, 105);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void delete_ValidId_ReturnsNoContent() throws Exception {
        Order order = orderService.create(new Order(OrderStatus.ORDERED,
                LocalDateTime.parse("2021-10-10T14:52"), 500.00, restaurantTableService.findById(1)));

        int size = orderService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE,
                httpEntity, OrderDto.class, order.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(size - 1, orderService.findAll().size());

    }

    @Test
    public void delete_InvalidId_ReturnsNotFound() {
        int size = orderService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE,
                httpEntity, OrderDto.class, 5000);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, orderService.findAll().size());

    }

    @Test
    public void update_ValidId_ReturnsOk() throws Exception {
        HttpEntity<UpdateOrderDto> httpEntity = new HttpEntity<>(new UpdateOrderDto(OrderStatus.PAID, 3000.00), headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT,
                httpEntity, OrderDto.class, 1);

        OrderDto orderDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(OrderStatus.PAID, OrderStatus.findType(orderDto.getStatus()));
        assertEquals(3000.00, orderDto.getPrice());

        Order order = orderService.findById(1);
        assertEquals(OrderStatus.PAID, order.getStatus());
        assertEquals(3000.00, order.getPrice());

        order.setPrice(1520.00);
        order.setDateOfOrder(LocalDateTime.parse("2021-11-19T14:15"));
        order.setStatus(OrderStatus.ORDERED);
        orderService.update(order, 1);

    }

    @Test
    public void update_InvalidId_ReturnsNotFound() {
        HttpEntity<UpdateOrderDto> httpEntity = new HttpEntity<>(new UpdateOrderDto(OrderStatus.FINISHED, 3000.00),
                headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT,
                httpEntity, OrderDto.class, 8523);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void getFilteredOrdersByStatus_ValidStatus_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<RestResponsePage<OrderDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<RestResponsePage<OrderDto>> responseEntity = restTemplate.exchange(URL_PREFIX + "/filter/{id}/{status}?page={page}&size={size}", HttpMethod.GET,
                httpEntity, responseType, 7, "Poručeno", 0, 8);

        List<OrderDto> orderDtoList = responseEntity.getBody().getContent();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Poručeno", orderDtoList.get(0).getStatus());

    }

    @Test
    public void getFilteredOrdersByStatus_InvalidStatus_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange(URL_PREFIX + "/filter/{id}/{status}?page={page}&size={size}", HttpMethod.GET,
                httpEntity, Object.class, 7, " ", 0, 8);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    public void getOrdersByWaiter_ValidWaiterId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ParameterizedTypeReference<RestResponsePage<OrderDto>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<RestResponsePage<OrderDto>> responseEntity = restTemplate.exchange(URL_PREFIX + "/by-waiter/{id}?page={page}&size={size}", HttpMethod.GET,
                httpEntity, responseType, 7, 0, 8);

        List<OrderDto> orderDtoList = responseEntity.getBody().getContent();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, orderDtoList.size());

    }

    @Test
    public void getOrdersByWaiter_InvalidWaiterId_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange(URL_PREFIX + "/by-waiter/{id}?page={page}&size={size}", HttpMethod.GET,
                httpEntity, Object.class, -7, 0, 8);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }


    @Test
    public void updateStatus_ValidStatus_ReturnsOk() throws Exception {

        HttpEntity<?> httpEntity = new HttpEntity<>("Plaćeno", headers);

        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/status/{id}",
                HttpMethod.POST, httpEntity, OrderDto.class, 1);

        OrderDto orderDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Plaćeno", orderDto.getStatus());

        Order order = orderService.findById(1);
        assertEquals(OrderStatus.PAID, order.getStatus());

        order.setStatus(OrderStatus.ORDERED);
        orderService.update(order, 1);

    }

    @Test
    public void update_InvalidStatus_ReturnsNotFound() {
        HttpEntity<?> httpEntity = new HttpEntity<>(" ", headers);

        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/status/{id}",
                HttpMethod.POST, httpEntity, OrderDto.class, 1);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

}
