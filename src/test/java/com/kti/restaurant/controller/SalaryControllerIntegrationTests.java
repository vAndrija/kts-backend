package com.kti.restaurant.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.salary.CreateSalaryDto;
import com.kti.restaurant.dto.salary.SalaryDto;
import com.kti.restaurant.model.Salary;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.UserService;
import com.kti.restaurant.service.implementation.SalaryService;
import com.kti.restaurant.utils.GsonUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class SalaryControllerIntegrationTests {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private SalaryService salaryService;
	
	@Autowired
	private UserService userService;
	
	private HttpHeaders headers;
	
	private String accessToken;
	
	private static final String URI_PREFIX = "/api/v1/salaries";
	
	@BeforeEach
	public void login() {
		ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("sarajovic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();

        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

	}
	
	@Test
	public void findAll_ReturnsOk() {
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers); 
		ResponseEntity<SalaryDto[]> responseEntity = restTemplate
				.exchange(URI_PREFIX, HttpMethod.GET, httpEntity, SalaryDto[].class);
		
		List<SalaryDto> salaries = List.of(responseEntity.getBody());
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(8, salaries.size());
	}
	
	@Test
	public void findById_ValidSalaryId_ReturnsOk() {
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers); 
		ResponseEntity<SalaryDto> responseEntity = restTemplate
				.exchange(URI_PREFIX + "/{id}", HttpMethod.GET, httpEntity, SalaryDto.class, 1);
		
		SalaryDto salary = responseEntity.getBody();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(45000.00, salary.getValue());
		assertEquals("mirkomiric@gmail.com", salary.getEmail());
	}
	
	@Test
	public void findById_InvalidSalaryId_ReturnsNotFound() {
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers); 
		ResponseEntity<SalaryDto> responseEntity = restTemplate
				.exchange(URI_PREFIX + "/{id}", HttpMethod.GET, httpEntity, SalaryDto.class, 9);
		
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}
	
	@Test
	public void createSalary_ValidSalary_ReturnsCreated() throws Exception {
		int size = salaryService.findAll().size();
		
		CreateSalaryDto salary = new CreateSalaryDto(45000.00, LocalDate.parse("2021-12-01"), 
				LocalDate.parse("2022-12-11"), "mirkomiric@gmail.com");
		
		
		HttpEntity<CreateSalaryDto> httpEntity = new HttpEntity<CreateSalaryDto>(salary, headers);
		
		ResponseEntity<SalaryDto> entity = restTemplate
				.postForEntity(URI_PREFIX, httpEntity, SalaryDto.class);
		
		SalaryDto createdSalaryDto = entity.getBody();
		
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
		assertEquals(size + 1, salaryService.findAll().size());
		assertEquals("mirkomiric@gmail.com", createdSalaryDto.getEmail());
		assertEquals(45000.00, createdSalaryDto.getValue());
		
		salaryService.delete(createdSalaryDto.getId());
		assertEquals(size, salaryService.findAll().size());
	} 
	
	@Test
	public void createSalary_InvalidUserEmail_ReturnsNotFound() {
		int size = salaryService.findAll().size();
		
		CreateSalaryDto salary = new CreateSalaryDto(45000.00, LocalDate.parse("2021-12-01"), 
				LocalDate.parse("2022-12-11"), "mirkomirkovic@gmail.com");
		
		HttpEntity<CreateSalaryDto> httpEntity = new HttpEntity<CreateSalaryDto>(salary, headers);
		
		ResponseEntity<Object> entity = restTemplate.postForEntity(URI_PREFIX, httpEntity, Object.class);
		
		assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
		assertEquals(size, salaryService.findAll().size());
	}
	
	@ParameterizedTest
	@MethodSource("provideSalaryDto")
	public void createSalary_InvalidParameters_ReturnsBadRequest(Double value, LocalDate startDate, LocalDate endDate, String email , String bodyParameter, String errorMessage) {
		int size = salaryService.findAll().size();
		
		CreateSalaryDto salary = new CreateSalaryDto(value, startDate, endDate, email);
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(GsonUtils.ToJson(salary), headers);
		ParameterizedTypeReference<HashMap<String, String>> responseType = new ParameterizedTypeReference<HashMap<String, String>>() { };
		
		ResponseEntity<HashMap<String, String>> entity = restTemplate
				.exchange(URI_PREFIX, HttpMethod.POST, httpEntity, responseType);
		

		HashMap<String, String> body = entity.getBody();
		
		assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
		assertEquals(errorMessage, body.get(bodyParameter));
		assertEquals(size, salaryService.findAll().size());
	}
	
	
	@ParameterizedTest
	@MethodSource("provideSalaryDto")
	public void updateSalary_InvalidParameters_ReturnsBadRequest(Double value, LocalDate startDate, LocalDate endDate, 
			String email , String bodyParameter, String errorMessage) {
		int size = salaryService.findAll().size();
		
		CreateSalaryDto salary = new CreateSalaryDto(value, startDate, endDate, email);
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(GsonUtils.ToJson(salary), headers);
		ParameterizedTypeReference<HashMap<String, String>> responseType = new ParameterizedTypeReference<HashMap<String, String>>() { };
		
		ResponseEntity<HashMap<String, String>> entity = restTemplate
				.exchange(URI_PREFIX + "/{id}", HttpMethod.PUT, httpEntity, responseType, 1);
		

		HashMap<String, String> body = entity.getBody();
		
		assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
		assertEquals(errorMessage, body.get(bodyParameter));
		assertEquals(size, salaryService.findAll().size());
	}
	
	private static Stream<Arguments> provideSalaryDto() {
		
		return Stream.of(
				Arguments.of(null, LocalDate.parse("2021-12-01"), 
						LocalDate.parse("2022-12-11"), "mirkomiric@gmail.com", "value" ,"Value should not be null"),
				Arguments.of(0.00, LocalDate.parse("2021-12-01"), 
						LocalDate.parse("2022-12-11"), "mirkomiric@gmail.com", "value", "Salary should not be negative value."),
				Arguments.of(45000.00, null, 
						LocalDate.parse("2022-12-11"), "mirkomiric@gmail.com", "startDate", "Start date of salary should not be null"),
				Arguments.of(45000.00, LocalDate.parse("2021-12-11"), 
						null, "mirkomiric@gmail.com", "endDate", "End date of salary should not be null"),
				Arguments.of(45000.00, LocalDate.parse("2021-12-11"), 
						LocalDate.parse("2022-12-11"), null, "userEmail", "User email should not be null or empty")
				
				);
	}
	@Test
	public void updateSalary_ValidSalary_ReturnsOk() throws Exception {		
		CreateSalaryDto salary = new CreateSalaryDto(45000.00, LocalDate.parse("2021-12-01"), 
				LocalDate.parse("2022-12-11"), "mirkomiric@gmail.com");
		
		Salary oldSalary = salaryService.findById(1);
		
		HttpEntity<CreateSalaryDto> httpEntity = new HttpEntity<CreateSalaryDto>(salary, headers);
		
		ResponseEntity<SalaryDto> entity = restTemplate
				.exchange(URI_PREFIX + "/{id}", HttpMethod.PUT, httpEntity, SalaryDto.class, 1);
		
		SalaryDto createdSalaryDto = entity.getBody();
		
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(45000.00, createdSalaryDto.getValue());
		assertEquals(LocalDate.parse("2021-12-01"), createdSalaryDto.getStartDate());
		assertEquals(LocalDate.parse("2022-12-11"), createdSalaryDto.getEndDate());
		
		salaryService.update(oldSalary, 1);
		
	} 
	
	@Test
	public void deleteTableReservation_ValidId_ReturnsNoContent() throws Exception {		
		Salary salary = new Salary(45000.00, LocalDate.parse("2021-12-11"), LocalDate.parse("2022-12-11"), userService.findById(1));
		Salary createdSalary = salaryService.create(salary);
		
		int size = salaryService.findAll().size();
		
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Object> entity = restTemplate
				.exchange(URI_PREFIX + "/{id}", HttpMethod.DELETE, httpEntity, Object.class, createdSalary.getId());
		
		assertEquals(HttpStatus.NO_CONTENT, entity.getStatusCode());
		assertEquals(size-1, salaryService.findAll().size());
	} 
	
	@Test
    public void delete_InvalidId_ReturnsNotFound() throws Exception {
        int size = salaryService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = restTemplate
        		.exchange(URI_PREFIX + "/{id}", HttpMethod.DELETE, httpEntity,
                Object.class, 9);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, salaryService.findAll().size());
    }

}
