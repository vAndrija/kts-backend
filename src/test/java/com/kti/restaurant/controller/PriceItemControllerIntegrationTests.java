package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.priceitem.PriceItemDto;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.PriceItem;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.PriceItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class PriceItemControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PriceItemService priceItemService;

    private String accessToken;

    private HttpHeaders headers;

    private final String URL_PREFIX = "/api/v1/price-items";

    @BeforeEach
    public void login() {
        ResponseEntity<UserTokenState> responseEntity = restTemplate.postForEntity("/api/v1/auth/login",
                new JwtAuthenticationRequest("sarajovic@gmail.com", "123"),
                UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();

        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
    }

    @Test
    public void createPriceItem_ValidPriceItem_ReturnsCreated() throws Exception {
        int size = priceItemService.findAll().size();

        HttpEntity<PriceItemDto> httpEntity = new HttpEntity<>(new PriceItemDto(Double.valueOf(200), LocalDate.parse("2021-11-05"),
                LocalDate.parse("2022-11-05"), 2, true, Double.valueOf(120)), headers);
        ResponseEntity<PriceItem> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, PriceItem.class);

        PriceItem priceItem = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(size + 1, priceItemService.findAll().size());

        assertEquals(Double.valueOf(200), priceItem.getValue());
        assertEquals(Double.valueOf(120), priceItem.getPreparationValue());
        assertEquals(LocalDate.now(), priceItem.getStartDate());
        assertEquals(null, priceItem.getEndDate());
        assertEquals(2, priceItem.getMenuItem().getId());
        assertEquals(true, priceItem.getCurrent());

        priceItemService.delete(priceItem.getId());
    }

    @Test
    public void createPriceItem_PriceItemWithInvalidMenuItemId_ReturnsNotFound() {
        int size = priceItemService.findAll().size();

        HttpEntity<PriceItemDto> httpEntity = new HttpEntity<>(new PriceItemDto(Double.valueOf(200), LocalDate.parse("2021-11-05"),
                LocalDate.parse("2022-11-05"), 25, true, Double.valueOf(120)), headers);
        ResponseEntity<PriceItem> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, PriceItem.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, priceItemService.findAll().size());
    }

    @Test
    public void getPriceItemById_ValidPriceItemId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<PriceItem> responseEntity = restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.GET, httpEntity, PriceItem.class);

        PriceItem priceItem = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Double.valueOf(180), priceItem.getValue());
        assertEquals(LocalDate.parse("2021-11-18"), priceItem.getStartDate());
        assertEquals(LocalDate.parse("2021-12-18"), priceItem.getEndDate());
        assertEquals(true, priceItem.getCurrent());
        assertEquals(Double.valueOf(100), priceItem.getPreparationValue());
    }

    @Test
    public void getPriceItemById_InvalidPriceItemId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<PriceItem> responseEntity = restTemplate.exchange(URL_PREFIX + "/21", HttpMethod.GET, httpEntity, PriceItem.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getPriceItems_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<PriceItem[]> responseEntity = restTemplate.exchange(URL_PREFIX, HttpMethod.GET, httpEntity, PriceItem[].class);

        List<PriceItem> priceItems = List.of(responseEntity.getBody());
        PriceItem priceItem = priceItems.get(0);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(13, priceItems.size());
        assertEquals(Double.valueOf(180), priceItem.getValue());
        assertEquals(LocalDate.parse("2021-11-18"), priceItem.getStartDate());
        assertEquals(LocalDate.parse("2021-12-18"), priceItem.getEndDate());
        assertEquals(true, priceItem.getCurrent());
        assertEquals(Double.valueOf(100), priceItem.getPreparationValue());
    }

    @Test
    public void updatePriceItem_ValidPriceItem_ReturnsOk() throws Exception {
        HttpEntity<PriceItemDto> httpEntity = new HttpEntity<>(new PriceItemDto(Double.valueOf(200), LocalDate.parse("2021-11-18"),
                LocalDate.parse("2021-12-18"), 1, false, Double.valueOf(100)), headers);
        ResponseEntity<PriceItem> responseEntity = restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.PUT, httpEntity, PriceItem.class);

        PriceItem priceItem = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(LocalDate.now(), priceItem.getEndDate());

        PriceItem updatedPriceItem = priceItemService.findById(1);
        assertEquals(false, updatedPriceItem.getCurrent());
        assertEquals(LocalDate.now(), updatedPriceItem.getEndDate());

        updatedPriceItem.setCurrent(true);
        updatedPriceItem.setEndDate(LocalDate.parse("2021-12-18"));
        priceItemService.update(updatedPriceItem, 1);
    }

    @Test
    public void updatePriceItem_InvalidPriceItemId_ReturnsNotFound() {
        HttpEntity<PriceItemDto> httpEntity = new HttpEntity<>(new PriceItemDto(Double.valueOf(200), LocalDate.parse("2021-11-18"),
                LocalDate.parse("2021-12-18"), 1, true, Double.valueOf(100)), headers);
        ResponseEntity<PriceItem> responseEntity = restTemplate.exchange(URL_PREFIX + "/25", HttpMethod.PUT, httpEntity, PriceItem.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void deletePriceItem_ValidPriceItemId_ReturnsNoContent() throws Exception {
        PriceItem priceItem = priceItemService.create(new PriceItem(null, LocalDate.parse("2021-11-18"),
                LocalDate.parse("2021-12-18"), new MenuItem(), true, Double.valueOf(100)));

        int size = priceItemService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<PriceItem> responseEntity = restTemplate.exchange(URL_PREFIX + "/1", HttpMethod.DELETE, httpEntity, PriceItem.class);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(size - 1, priceItemService.findAll().size());
    }

    @Test
    public void deletePriceItem_InvalidPriceItemId_ReturnsNotFound() {
        int size = priceItemService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<PriceItem> responseEntity = restTemplate.exchange(URL_PREFIX + "/25", HttpMethod.DELETE, httpEntity, PriceItem.class);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, priceItemService.findAll().size());
    }
}
