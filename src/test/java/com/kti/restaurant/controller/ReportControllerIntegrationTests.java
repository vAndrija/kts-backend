package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.ReportService;
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
public class ReportControllerIntegrationTests {

    @Autowired
    private ReportService reportService;

    @Autowired
    private TestRestTemplate restTemplate;

    private String accessToken;

    private HttpHeaders headers;

    private final String URL_PREFIX = "/api/v1/reports";

    @BeforeEach
    public void login() {
        ResponseEntity<UserTokenState> responseEntity = restTemplate.postForEntity("/api/v1/auth/login",
                new JwtAuthenticationRequest("sarajovic@gmail.com", "123"), UserTokenState.class);

        accessToken = responseEntity.getBody().getAccessToken();
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
    }

    @Test
    public void getYearlyReportForMealAndDrinkCosts_ValidYear_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Double[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/meal-drink-costs",
                HttpMethod.GET, httpEntity, Double[].class, 2021);

        List<Double> costsPerMonths = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(12, costsPerMonths.size());
        assertEquals(Double.valueOf(1860), costsPerMonths.get(10));
        assertEquals(Double.valueOf(0), costsPerMonths.get(1));
    }

    @Test
    public void getYearlyReportForMealAndDrinkCosts_InvalidYear_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/meal-drink-costs",
                HttpMethod.GET, httpEntity, Void.class, Integer.valueOf(-1));

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getYearlyReportForMealAndDrinkCosts_YearWithoutOrderItems_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Double[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/meal-drink-costs",
                HttpMethod.GET, httpEntity, Double[].class, 2001);

        List<Double> costsPerMonths = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        costsPerMonths.forEach(costPerMonth -> {
            assertEquals(Double.valueOf(0), costPerMonth);
        });
    }

    @Test
    public void getMonthlyReportForMealAndDrinkCosts_ValidYear_ValidMonth_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Double[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-costs",
                HttpMethod.GET, httpEntity, Double[].class, 2021, 11);

        List<Double> costPerDays = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(30, costPerDays.size());
        assertEquals(Double.valueOf(1860), costPerDays.get(18));
    }

    @Test
    public void getMonthlyReportForMealAndDrinkCosts_InvalidYear_ValidMonth_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-costs",
                HttpMethod.GET, httpEntity, Void.class, -1, 11);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyReportForMealAndDrinkCosts_InvalidYear_InvalidMonth_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-costs",
                HttpMethod.GET, httpEntity, Void.class, -1, -11);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyReportForMealAndDrinkCosts_ValidYear_InvalidMonth_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-costs",
                HttpMethod.GET, httpEntity, Void.class, 2021, -11);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyReportForMealAndDrinkCosts_ValidYear_MonthWithoutOrders_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Double[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-costs",
                HttpMethod.GET, httpEntity, Double[].class, 2021, 5);

        List<Double> costsPerDay = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(31, costsPerDay.size());
        costsPerDay.forEach(costPerDay -> {
            assertEquals(Double.valueOf(0), costPerDay);
        });
    }

    @Test
    public void getYearlyCostBenefitRatio_ValidYear_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Double[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/cost-benefit-ratio",
                HttpMethod.GET, httpEntity, Double[].class, 2021);

        List<Double> ratioPerMonths = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(12, ratioPerMonths.size());
        assertEquals(Double.valueOf(0), ratioPerMonths.get(0));
        assertEquals(Double.valueOf(-135000), ratioPerMonths.get(1));
        assertEquals(Double.valueOf(-195000), ratioPerMonths.get(8));
        assertEquals(Double.valueOf(-242050), ratioPerMonths.get(10));
        assertEquals(Double.valueOf(-464000), ratioPerMonths.get(11));
    }

    @Test
    public void getYearlyCostBenefitRatio_YearWithoutOrdersAndPriceItems_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Double[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/cost-benefit-ratio",
                HttpMethod.GET, httpEntity, Double[].class, 2001);

        List<Double> ratioPerMonths = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(12, ratioPerMonths.size());
        ratioPerMonths.forEach(ratio -> {
            assertEquals(Double.valueOf(0), ratio);
        });
    }

    @Test
    public void getYearlyCostBenefitRatio_InvalidYear_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/cost-benefit-ratio",
                HttpMethod.GET, httpEntity, Void.class, -1);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyReportForCostBenefitRatio_ValidYear_ValidMonth_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Double[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/cost-benefit-ratio",
                HttpMethod.GET, httpEntity, Double[].class, 2021, 11);

        List<Double> ratioPerDays = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(30, ratioPerDays.size());
        assertEquals(Double.valueOf(-8167), ratioPerDays.get(0));
        assertEquals(Double.valueOf(-5217), ratioPerDays.get(18));
    }

    @Test
    public void getMonthlyReportForCostBenefitRatio_YearMonthWithoutOrdersAndPriceItems_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Double[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/cost-benefit-ratio",
                HttpMethod.GET, httpEntity, Double[].class, 2001, 6);

        List<Double> ratioPerDays = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(30, ratioPerDays.size());
        ratioPerDays.forEach(ratio -> {
            assertEquals(Double.valueOf(0), ratio);
        });
    }

    @Test
    public void getMonthlyReportForCostBenefitRatio_InvalidYear_ValidMonth_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/cost-benefit-ratio",
                HttpMethod.GET, httpEntity, Void.class, -1, 11);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyReportForCostBenefitRatio_ValidYear_InvalidMonth_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/cost-benefit-ratio",
                HttpMethod.GET, httpEntity, Void.class, 2021, -11);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyReportForCostBenefitRatio_InvalidYear_InvalidMonth_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/cost-benefit-ratio",
                HttpMethod.GET, httpEntity, Void.class, -1, -11);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getYearlyPreparingTimeReport_ValidYear_ValidUserId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Integer[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/time-preparing/{userId}",
                HttpMethod.GET, httpEntity, Integer[].class, 2021, 2);

        List<Integer> preparationTimePerMonths = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(12, preparationTimePerMonths.size());
        assertEquals(0, preparationTimePerMonths.get(0));
        assertEquals(2, preparationTimePerMonths.get(10));
    }

    @Test
    public void getYearlyPreparingTimeReport_YearWithoutOrders_ValidUserId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Integer[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/time-preparing/{userId}",
                HttpMethod.GET, httpEntity, Integer[].class, 2001, 2);

        List<Integer> preparationTimePerMonths = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(12, preparationTimePerMonths.size());
        preparationTimePerMonths.forEach(time -> {
            assertEquals(Integer.valueOf(0), time);
        });
    }

    @Test
    public void getYearlyPreparingTimeReport_InvalidYear_ValidUserId_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/time-preparing/{userId}",
                HttpMethod.GET, httpEntity, Void.class, -1, 2);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getYearlyPreparingTimeReport_ValidYear_InvalidUserId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/time-preparing/{userId}",
                HttpMethod.GET, httpEntity, Void.class, 2021, 35);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getYearlyPreparingTimeReport_ValidYear_InvalidUserType_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/time-preparing/{userId}",
                HttpMethod.GET, httpEntity, Void.class, 2021, 1);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getYearlyPreparingTimeReport_InvalidYear_InvalidUserId_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/time-preparing/{userId}",
                HttpMethod.GET, httpEntity, Void.class, -1, 35);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyPreparingTimeReport_ValidYear_ValidMonth_ValidUserId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Integer[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/time-preparing{userId}",
                HttpMethod.GET, httpEntity, Integer[].class, 2021, 11, 4);

        List<Integer> preparationTimePerDays = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(30, preparationTimePerDays.size());
        assertEquals(18, preparationTimePerDays.get(18));
    }

    @Test
    public void getMonthlyPreparingTimeReport_InalidYear_ValidMonth_ValidUserId_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/time-preparing{userId}",
                HttpMethod.GET, httpEntity, Void.class, -2021, 11, 4);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyPreparingTimeReport_ValidYear_InvalidMonth_ValidUserId_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/time-preparing{userId}",
                HttpMethod.GET, httpEntity, Void.class, 2021, -11, 4);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyPreparingTimeReport_ValidYear_ValidMonth_InvalidUserId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/time-preparing{userId}",
                HttpMethod.GET, httpEntity, Void.class, 2021, 11, 24);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyPreparingTimeReport_ValidYear_ValidMonth_InvalidUserType_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/time-preparing{userId}",
                HttpMethod.GET, httpEntity, Void.class, 2021, 11, 1);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyPreparingTimeReport_InvalidYear_InvalidMonth_InvalidUserId_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/time-preparing{userId}",
                HttpMethod.GET, httpEntity, Void.class, -2021, -11, 24);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyPreparingTimeReport_InvalidYear_ValidMonth_InvalidUserId_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/time-preparing{userId}",
                HttpMethod.GET, httpEntity, Void.class, -2021, 11, 24);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyPreparingTimeReport_ValidYear_InvalidMonth_InvalidUserId_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/time-preparing{userId}",
                HttpMethod.GET, httpEntity, Void.class, 2021, -11, 24);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getMonthlyPreparingTimeReport_InvalidYear_InvalidMonth_ValidUserId_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/time-preparing{userId}",
                HttpMethod.GET, httpEntity, Void.class, -2021, -11, 4);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    @Test
    public void getYearlyReportForMealAndDrinkSales_ValidYear_ValidMenuItemId_ReturnsStatusOk() {
    	HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Integer[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Integer[].class, 2021, 1);

        List<Integer> salesPerMonth = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(12, salesPerMonth.size());
        assertEquals(3, salesPerMonth.get(10));
    }
    
    @Test
    public void getYearlyReportForMealAndDrinkSales_YearWithoutOrders_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Integer[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Integer[].class, 2001, 1);

        List<Integer> salesPerMonth = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(12, salesPerMonth.size());
        salesPerMonth.forEach(ratio -> {
            assertEquals(Integer.valueOf(0), ratio);
        });
    }
    
    @Test
    public void getYearlyReportForMealAndDrinkSales_InvalidYear_ValidMenuItemId_ReturnsStatusBadRequest() {
    	HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Void.class, -1, 1);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    @Test
    public void getYearlyReportForMealAndDrinkSales_ValidYear_InvalidMenuItemId_ReturnsStatusNotFound() {
    	HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Void.class, 2021, 15);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    
    @Test
    public void getMonthlyReportForMealAndDrinkSales_ValidParameters_ReturnsStatusOk() {
    	HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Integer[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Integer[].class, 2021,11, 1);

        List<Integer> salesPerDay = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(30, salesPerDay.size());
        assertEquals(3, salesPerDay.get(18));
    }
    
    @Test
    public void getMonthlyReportForMealAndDrinkSales_YearWithoutOrders_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Integer[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Integer[].class, 2001, 11, 1);

        List<Integer> salesPerDay = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(30, salesPerDay.size());
        salesPerDay.forEach(ratio -> {
            assertEquals(Integer.valueOf(0), ratio);
        });
    }
    
    @Test
    public void getMonthlyReportForMealAndDrinkSales_InvalidYear_ReturnsStatusBadRequest() {
    	HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Void.class, -2021,11, 1);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    @Test
    public void getMonthlyReportForMealAndDrinkSales_InvalidMonth_ReturnsStatusBadRequest() {
    	HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Void.class, 2021,13, 1);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    @Test
    public void getMonthlyReportForMealAndDrinkSales_InvalidMenuItemId_ReturnsStatusNotFound() {
    	HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Void.class, 2021,11, 15);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    
    
    
}
