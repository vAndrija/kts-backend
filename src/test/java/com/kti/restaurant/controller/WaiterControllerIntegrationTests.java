package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.waiter.WaiterCreateDto;
import com.kti.restaurant.dto.waiter.WaiterDto;
import com.kti.restaurant.dto.waiter.WaiterUpdateDto;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.model.Waiter;
import com.kti.restaurant.service.implementation.WaiterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class WaiterControllerIntegrationTests {

    private static final String URL_PREFIX = "/api/v1/waiter";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WaiterService waiterService;

    private String accessToken;

    private HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    public void login() {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("mirkomiric@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        headers.add("Authorization", "Bearer " + accessToken);
    }

    @Test
    public void create_UniqueEmail_ReturnsCreated() throws Exception {
        int size = waiterService.findAll().size();

        HttpEntity<WaiterCreateDto> httpEntity = new HttpEntity<>(new WaiterCreateDto("Aleksa", "Maric",
                "111111", "152487", "aleksamaric@gmail.com"), headers);
        ResponseEntity<WaiterDto> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, WaiterDto.class);

        WaiterDto waiterDto = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Aleksa", waiterDto.getName());
        assertEquals("Maric", waiterDto.getLastName());
        assertEquals("111111", waiterDto.getPhoneNumber());
        assertEquals("152487", waiterDto.getAccountNumber());
        assertEquals("aleksamaric@gmail.com", waiterDto.getEmailAddress());

        List<Waiter> waiterList = waiterService.findAll();
        assertEquals(size + 1, waiterList.size());
        assertEquals("aleksamaric@gmail.com", waiterList.get(size).getEmailAddress());

        waiterService.delete(waiterDto.getId());

    }

    @Test
    public void create_NonUniqueEmail_ReturnsConflict() {
        HttpEntity<WaiterCreateDto> httpEntity = new HttpEntity<>(new WaiterCreateDto("Ana", "Popovic",
                "111111", "152487", "anapopovic@gmail.com"), headers);
        ResponseEntity<Waiter> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, Waiter.class);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void getWaiters_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<WaiterDto[]> responseEntity = restTemplate.exchange(URL_PREFIX, HttpMethod.GET, httpEntity,
                WaiterDto[].class);

        WaiterDto[] waiterDtos = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, waiterDtos.length);
        assertEquals("jovanpetrovic@gmail.com", waiterDtos[0].getEmailAddress());
        assertEquals("anapopovic@gmail.com", waiterDtos[1].getEmailAddress());
    }

    @Test
    public void getWaiter_ValidId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<WaiterDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, WaiterDto.class, 7);

        WaiterDto waiterDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("jovanpetrovic@gmail.com", waiterDto.getEmailAddress());
        assertEquals("jovan", waiterDto.getName());
        assertEquals("petrovic", waiterDto.getLastName());
        assertEquals("0607425922", waiterDto.getPhoneNumber());
        assertEquals("22365612316263", waiterDto.getAccountNumber());

    }

    @Test
    public void getWaiter_InvalidId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<WaiterDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, WaiterDto.class, 1);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void updateWaiter_ValidId_ReturnsOk() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("anapopovic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<WaiterUpdateDto> httpEntity = new HttpEntity<>(new WaiterUpdateDto("ana", "savic",
                "152487", "8795613"), httpHeaders);
        ResponseEntity<WaiterDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                WaiterDto.class, 8);

        WaiterDto waiterDto = responseEntity1.getBody();
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals("ana", waiterDto.getName());
        assertEquals("savic", waiterDto.getLastName());
        assertEquals("152487", waiterDto.getPhoneNumber());
        assertEquals("8795613", waiterDto.getAccountNumber());

        Waiter waiter = waiterService.findById(8);
        assertEquals("ana", waiter.getName());
        assertEquals("savic", waiter.getLastName());
        assertEquals("152487", waiter.getPhoneNumber());
        assertEquals("8795613", waiter.getAccountNumber());

        waiter.setLastName("popovic");
        waiter.setPhoneNumber("0627412922");
        waiter.setAccountNumber("22005612314563");

        waiterService.update(waiter, 8);

    }

    @Test
    public void update_InvalidId_AnotherUser_ReturnsForbidden() {
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("anapopovic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<WaiterUpdateDto> httpEntity = new HttpEntity<>(new WaiterUpdateDto("ana", "savic",
                "152487", "8795613"), httpHeaders);
        ResponseEntity<WaiterDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                WaiterDto.class, 100);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity1.getStatusCode());
    }

    @Test
    public void update_InvalidId_ReturnsNotFound() {
        HttpHeaders httpHeaders = new HttpHeaders();
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("sarajovic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        HttpEntity<WaiterUpdateDto> httpEntity = new HttpEntity<>(new WaiterUpdateDto("ana", "savic",
                "152487", "8795613"), httpHeaders);
        ResponseEntity<WaiterDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                WaiterDto.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity1.getStatusCode());
    }

    @Test
    public void delete_ValidId_ReturnsNoContent() throws Exception {
        Waiter waiter = waiterService.create(new Waiter("Aleksa", "Maric",
                "111111", "152487", "aleksamaric@gmail.com"));
        int size = waiterService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<WaiterDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE, httpEntity,
                WaiterDto.class, waiter.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(size - 1, waiterService.findAll().size());
    }

    @Test
    public void delete_InvalidId_ReturnsNotFound() throws Exception {
        int size = waiterService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE, httpEntity,
                Object.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, waiterService.findAll().size());
    }


}
