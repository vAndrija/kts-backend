package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.model.UserTokenState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;


import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class ReportControllerIntegrationTests {

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
    public void getMonthlyReportForMealAndDrinkCosts_ValidYearValidMonth_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Double[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-costs",
                HttpMethod.GET, httpEntity, Double[].class, 2021, 11);

        List<Double> costPerDays = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(30, costPerDays.size());
        assertEquals(Double.valueOf(1860), costPerDays.get(18));
    }

    private static Stream<Arguments> provideInvalidParamteres() {
		
		return Stream.of(
				Arguments.of(-1, 11),
				Arguments.of(-1, -11), 
				Arguments.of(2021, -11),
				Arguments.of(2021, 0),
				Arguments.of(2021, 13)
				);
	}
    
    @ParameterizedTest
	@MethodSource("provideInvalidParamteres")
	public void getMonthlyReportForMealAndDrinkCosts_InvalidParameters_ReturnsBadRequest(Integer year, Integer month) {
    	 HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
         ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-costs",
                 HttpMethod.GET, httpEntity, Void.class, year, month);

         assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
    
    @Test
    public void getMonthlyReportForMealAndDrinkCosts_ValidYearMonthWithoutOrders_ReturnsOk() {
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
    public void getMonthlyReportForCostBenefitRatio_ValidYearValidMonth_ReturnsOk() {
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
    
    @ParameterizedTest
	@MethodSource("provideInvalidParamteres")
	public void getMonthlyReportForCostBenefitRatio_InvalidParameters_ReturnsBadRequest(Integer year, Integer month) {
    	 HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
         ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/cost-benefit-ratio",
                 HttpMethod.GET, httpEntity, Void.class, year, month);

         assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
	}
    
    @Test
    public void getYearlyPreparingTimeReport_ValidYearValidUserId_ReturnsOk() {
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
    public void getYearlyPreparingTimeReport_YearWithoutOrdersValidUserId_ReturnsOk() {
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
    public void getYearlyPreparingTimeReport_ValidYearInvalidUserId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/time-preparing/{userId}",
                HttpMethod.GET, httpEntity, Void.class, 2021, 35);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @ParameterizedTest
	@MethodSource("provideInvalidParamteresForYearlyPreparingTimeReport")
    public void getYearlyPreparingTimeReport_InvalidYearValidUserId_ReturnsBadRequest() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/time-preparing/{userId}",
                HttpMethod.GET, httpEntity, Void.class, -1, 2);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    private static Stream<Arguments> provideInvalidParamteresForYearlyPreparingTimeReport() {
		
		return Stream.of(
				Arguments.of(-1, 2),
				Arguments.of(2021, 1), 
				Arguments.of(-1, 35)
				);
	}

    @Test
    public void getMonthlyPreparingTimeReport_ValidYearValidMonthValidUserId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Integer[]> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/time-preparing{userId}",
                HttpMethod.GET, httpEntity, Integer[].class, 2021, 11, 4);

        List<Integer> preparationTimePerDays = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(30, preparationTimePerDays.size());
        assertEquals(18, preparationTimePerDays.get(18));
    }

    @Test
    public void getMonthlyPreparingTimeReport_ValidYearValidMonthInvalidUserId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/time-preparing{userId}",
                HttpMethod.GET, httpEntity, Void.class, 2021, 11, 24);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    
    @ParameterizedTest
	@MethodSource("provideInvalidParamteresForMonthlyPreparingTimeReport")
    public void getMonthlyPreparingTimeReport_InvalidParameters_ReturnsBadRequest(Integer year, Integer month, Integer userId) {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/time-preparing{userId}",
                HttpMethod.GET, httpEntity, Void.class, year,month, userId);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    private static Stream<Arguments> provideInvalidParamteresForMonthlyPreparingTimeReport() {
		
		return Stream.of(
				Arguments.of(-2021, 11, 4),
				Arguments.of(2021, 0, 4), 
				Arguments.of(2021, 13, 4),
				Arguments.of(2021, 11, 1),
				Arguments.of(-2021, 0, 24),
				Arguments.of(-2021, 11, 24),
				Arguments.of(2021, 0, 24),
				Arguments.of(-2021, 13, 24)
				);
	}

    @Test
    public void getYearlyReportForMealAndDrinkSales_ValidYearValidMenuItemId_ReturnsOk() {
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
    public void getYearlyReportForMealAndDrinkSales_InvalidYearValidMenuItemId_ReturnsBadRequest() {
    	HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Void.class, -1, 1);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    @Test
    public void getYearlyReportForMealAndDrinkSales_ValidYearInvalidMenuItemId_ReturnsNotFound() {
    	HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/yearly/{year}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Void.class, 2021, 15);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    
    @Test
    public void getMonthlyReportForMealAndDrinkSales_ValidParameters_ReturnsOk() {
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
    
    @ParameterizedTest
	@MethodSource("provideInvalidParamteresForMonthlyReportForMealAndDrinkSales")
    public void getMonthlyReportForMealAndDrinkSales_InvalidParameter_ReturnsStatusBadRequest(Integer year, Integer month, Integer menuItemId) {
    	HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Void.class, year, month, menuItemId);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    private static Stream<Arguments> provideInvalidParamteresForMonthlyReportForMealAndDrinkSales() {
		
		return Stream.of(
				Arguments.of(-2021, 11, 1),
				Arguments.of(2021, 13, 1), 
				Arguments.of(2021, 0, 1)
				);
	}
 
    @Test
    public void getMonthlyReportForMealAndDrinkSales_InvalidMenuItemId_ReturnsStatusNotFound() {
    	HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(URL_PREFIX + "/{year}/monthly/{month}/meal-drink-sales/{id}",
                HttpMethod.GET, httpEntity, Void.class, 2021,11, 15);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
    
    

    
}
