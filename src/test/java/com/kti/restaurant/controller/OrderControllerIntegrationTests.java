package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.order.CreateOrderDto;
import com.kti.restaurant.dto.order.OrderDto;
import com.kti.restaurant.dto.order.UpdateOrderDto;
import com.kti.restaurant.model.Order;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.model.enums.OrderStatus;
import com.kti.restaurant.service.implementation.OrderService;
import com.kti.restaurant.service.implementation.RestaurantTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class OrderControllerIntegrationTests {

    private  static final String URL_PREFIX = "/api/v1/orders";

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantTableService restaurantTableService;

    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;

    private HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void login(){
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("anapopovic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        headers.add("Authorization", "Bearer "+ accessToken);
    }

    @Test
    public void create_ValidOrder_ReturnsCreated() throws Exception {
        HttpEntity<CreateOrderDto> httpEntity = new HttpEntity<>(new CreateOrderDto(OrderStatus.ORDERED,
                LocalDateTime.parse("2021-10-10T14:52"), 500.00, 1, 7), headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.postForEntity(URL_PREFIX,httpEntity,OrderDto.class);

        OrderDto orderDto = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(OrderStatus.ORDERED, orderDto.getStatus());
        assertEquals(LocalDateTime.parse("2021-10-10T14:52"), orderDto.getDateOfOrder());
        assertEquals(500.00, orderDto.getPrice());
        assertEquals(1, orderDto.getTableId());

        orderService.delete(orderDto.getId());

    }

    @Test
    public void getOrders_ReturnsOk(){
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderDto[]> responseEntity = restTemplate.exchange(URL_PREFIX, HttpMethod.GET,
                httpEntity, OrderDto[].class);

        OrderDto[] orderDtoList = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(OrderStatus.ORDERED, orderDtoList[0].getStatus());
        assertEquals(OrderStatus.PAID, orderDtoList[1].getStatus());
        assertEquals(1520, orderDtoList[0].getPrice());
        assertEquals(1830, orderDtoList[1].getPrice());

    }

    @Test
    public void getOrder_ValidId_ReturnsOk(){
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX+"/{id}", HttpMethod.GET,
                httpEntity, OrderDto.class, 1);

        OrderDto orderDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(OrderStatus.ORDERED, orderDto.getStatus());
        assertEquals(LocalDateTime.parse("2021-11-19T14:15"), orderDto.getDateOfOrder());
        assertEquals(1520, orderDto.getPrice());
        assertEquals(3, orderDto.getTableId());
        assertEquals(7, orderDto.getWaiterId());
    }

    @Test
    public void getOrder_InvalidId_ReturnsNotFound(){
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX+"/{id}", HttpMethod.GET,
                httpEntity, OrderDto.class, 105);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void delete_ValidId_ReturnsNoContent() throws Exception {
        Order order = orderService.create(new Order(OrderStatus.ORDERED,
                LocalDateTime.parse("2021-10-10T14:52"), 500.00, restaurantTableService.findById(1)));

        int size = orderService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX+"/{id}", HttpMethod.DELETE,
                httpEntity, OrderDto.class, order.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(size - 1 , orderService.findAll().size());

    }

    @Test
    public void delete_InvalidId_ReturnsNotFound(){
        int size = orderService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX+"/{id}", HttpMethod.DELETE,
                httpEntity, OrderDto.class, 5000);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, orderService.findAll().size());
    }

    @Test
    public void update_ValidId_ReturnsOk() throws Exception {
        HttpEntity<UpdateOrderDto> httpEntity = new HttpEntity<>(new UpdateOrderDto(OrderStatus.PAID, 3000.00),headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX+"/{id}", HttpMethod.PUT,
                httpEntity, OrderDto.class, 1);

        OrderDto orderDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(OrderStatus.PAID, orderDto.getStatus());
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
    public void update_InvalidId_ReturnsNotFound(){
        HttpEntity<UpdateOrderDto> httpEntity = new HttpEntity<>(new UpdateOrderDto(OrderStatus.FINISHED, 3000.00),
                headers);
        ResponseEntity<OrderDto> responseEntity = restTemplate.exchange(URL_PREFIX+"/{id}", HttpMethod.PUT,
                httpEntity, OrderDto.class, 8523);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getFilteredOrdersByStatus_ValidStatus_ReturnsOk(){
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<OrderDto[]> responseEntity = restTemplate.exchange(URL_PREFIX+"/filter/{status}", HttpMethod.GET,
                httpEntity, OrderDto[].class, "ORDERED");

        OrderDto[] orderDtoList = responseEntity.getBody();

        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(OrderStatus.ORDERED, orderDtoList[0].getStatus());
        assertEquals(OrderStatus.ORDERED, orderDtoList[1].getStatus());
    }

    @Test
    public void getFilteredOrdersByStatus_InvalidStatus_ReturnsNotAcceptable(){
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange(URL_PREFIX+"/filter/{status}", HttpMethod.GET,
                httpEntity,Object.class, "PREPARATION");

        assertEquals(HttpStatus.NOT_ACCEPTABLE,responseEntity.getStatusCode());
    }

}
