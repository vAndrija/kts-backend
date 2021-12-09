package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.discount.DiscountDto;
import com.kti.restaurant.model.Discount;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class DiscountControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DiscountService discountService;

    private String accessToken;

    private HttpHeaders headers;

    @BeforeEach
    public void login() {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("sarajovic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + accessToken);
    }

    @Test
    public void getDiscounts_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        ResponseEntity<DiscountDto[]> responseEntity =
                restTemplate.exchange("/api/v1/discount", HttpMethod.GET, httpEntity, DiscountDto[].class);

        List<DiscountDto> discounts = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, discounts.size());
        assertEquals(10, discounts.get(0).getValue());
        assertEquals(8, discounts.get(0).getMenuItemId());
    }

    @Test
    public void getDiscountById_ValidDiscountId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        ResponseEntity<DiscountDto> responseEntity =
                restTemplate.exchange("/api/v1/discount/{id}", HttpMethod.GET, httpEntity, DiscountDto.class, 1);

        DiscountDto discountDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(10, discountDto.getValue());
        assertEquals(8, discountDto.getMenuItemId());
        assertEquals(false, discountDto.getCurrent());
        assertEquals(LocalDate.parse("2021-11-20"), discountDto.getStartDate());
        assertEquals(LocalDate.parse("2021-11-25"), discountDto.getEndDate());
    }

    @Test
    public void getDiscountById_InvalidDiscountId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        ResponseEntity<DiscountDto> responseEntity =
                restTemplate.exchange("/api/v1/discount/{id}", HttpMethod.GET, httpEntity, DiscountDto.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void create_ValidDiscount_ReturnsCreated() throws Exception {
        int size = discountService.findAll().size();

        HttpEntity<DiscountDto> httpEntity = new HttpEntity<DiscountDto>(new DiscountDto(15, LocalDate.parse("2021-11-11"),
                LocalDate.parse("2022-05-11"), 5, true), headers);
        ResponseEntity<Discount> responseEntity =
                restTemplate.postForEntity("/api/v1/discount", httpEntity,
                        Discount.class);

        Discount discount = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(15, discount.getValue());
        assertEquals(LocalDate.parse("2021-11-11"), discount.getStartDate());
        assertEquals(LocalDate.parse("2022-05-11"), discount.getEndDate());
        assertEquals(5, discount.getMenuItem().getId());
        assertEquals(true, discount.getCurrent());

        List<Discount> discounts = discountService.findAll();
        assertEquals(size+1, discounts.size());
        assertEquals(LocalDate.parse("2021-11-11"), discounts.get(size).getStartDate());

        discountService.delete(discount.getId());
    }

    @Test
    public void updateDiscount_ValidDiscount_ReturnsOk() throws Exception {
        HttpEntity<DiscountDto> httpEntity = new HttpEntity<DiscountDto>(new DiscountDto(15, LocalDate.parse("2021-11-20"),
                LocalDate.parse("2021-11-25"), 8, false), headers);
        ResponseEntity<Discount> responseEntity =
                restTemplate.exchange("/api/v1/discount/{id}", HttpMethod.PUT, httpEntity,
                        Discount.class, 1);

        Discount discount = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(15, discount.getValue());

        Discount updatedDiscount = discountService.findById(1);
        assertEquals(15, updatedDiscount.getValue());

        discount.setValue(10);
        discountService.update(discount, 1);
    }

    @Test
    public void updateDiscount_InvalidDiscountId_ReturnsNotFound() {
        HttpEntity<DiscountDto> httpEntity = new HttpEntity<DiscountDto>(new DiscountDto(15, LocalDate.parse("2021-11-11"),
                LocalDate.parse("2022-05-11"), 5, true), headers);
        ResponseEntity<Discount> responseEntity =
                restTemplate.exchange("/api/v1/discount/{id}", HttpMethod.PUT, httpEntity,
                        Discount.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void deleteDiscount_ValidDiscountIdI_ReturnsNoContent() throws Exception {
        Discount discount = discountService.create(new Discount(15, LocalDate.parse("2021-11-11"),
                LocalDate.parse("2022-05-11"), true, null));

        int size = discountService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        ResponseEntity<Discount> responseEntity =
                restTemplate.exchange("/api/v1/discount/{id}", HttpMethod.DELETE, httpEntity,
                        Discount.class, discount.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(size - 1, discountService.findAll().size());
    }

    @Test
    public void deleteDiscount_InvalidDiscountId_ReturnsNotFoundStatus() {
        int size = discountService.findAll().size();
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        ResponseEntity<Discount> responseEntity =
                restTemplate.exchange("/api/v1/discount/{id}", HttpMethod.DELETE, httpEntity,
                        Discount.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, discountService.findAll().size());
    }
}
