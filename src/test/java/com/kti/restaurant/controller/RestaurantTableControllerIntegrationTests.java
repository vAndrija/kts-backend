package com.kti.restaurant.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.RestaurantTableService;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class RestaurantTableControllerIntegrationTests {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private RestaurantTableService tableService;
	
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
		headers.add("Authorization", "Bearer " + accessToken);
	}
	
	@Test
	public void getRestaurantTable_ValidId_ReturnsStatusOk() {
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		
		ResponseEntity<RestaurantTable> entity = restTemplate
				.exchange("/api/v1/restaurant-table/1",HttpMethod.GET, httpEntity, RestaurantTable.class);
		
		RestaurantTable table = entity.getBody();
		
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertNotNull(table);
		assertEquals(Integer.valueOf(1), table.getId());
	}
	
	@Test
	public void getRestaurantTables_TablesExist_ReturnsStatusOk() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+ accessToken);
		
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
	
		ResponseEntity<RestaurantTable[]> entity = restTemplate
				.exchange("/api/v1/restaurant-table",HttpMethod.GET, httpEntity, RestaurantTable[].class);
		
		RestaurantTable[] tables = entity.getBody();
		
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertNotNull(tables);
		assertEquals(4, tables.length);
	}
	
	@Test
	public void createRestaurantTable_ValidInput_ReturnsStatusOk() throws Exception {
		int size = tableService.findAll().size();
		
    	RestaurantTable tableToAdd = new RestaurantTable(false, 1, 1, 1, 1);
    	
    	HttpEntity<RestaurantTable> httpEntity = new HttpEntity<RestaurantTable>(tableToAdd, headers);
    	
    	ResponseEntity<RestaurantTable> entity = restTemplate.postForEntity("/api/v1/restaurant-table", httpEntity, RestaurantTable.class);
    	
    	RestaurantTable table = entity.getBody();
    	
    	int newSize = tableService.findAll().size();
    	
    	assertNotNull(table);
    	assertEquals(HttpStatus.CREATED, entity.getStatusCode());
    	assertEquals(size + 1, newSize);
    	assertEquals(tableToAdd.getCapacity(), table.getCapacity());
    	
    	tableService.delete(table.getId());
	}
	
	@Test
	public void createRestaurantTable_InvalidTable_ReturnsStatusBadRequest() {
		RestaurantTable table = new RestaurantTable();
		
		HttpEntity<RestaurantTable> httpEntity = new HttpEntity<RestaurantTable>(table, headers);
		
		ResponseEntity<RestaurantTable> entity = restTemplate
				.exchange("/api/v1/restaurant-table", HttpMethod.POST, httpEntity, RestaurantTable.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
	}
	
	@Test
	public void updateRestaurantTable_ValidTable_ReturnsStatusOk() throws Exception {
		RestaurantTable table = new RestaurantTable(false, 1, 4, 1, 1);
		
		HttpEntity<RestaurantTable> httpEntity = new HttpEntity<RestaurantTable>(table, headers);
		
		ResponseEntity<RestaurantTable> entity = restTemplate
				.exchange("/api/v1/restaurant-table/{id}", HttpMethod.PUT, httpEntity, RestaurantTable.class, 1);
		
		RestaurantTable updatedTable = entity.getBody();
		
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(Integer.valueOf(1), updatedTable.getxCoordinate());
		
		RestaurantTable returnTable = tableService.findById(1);
		returnTable.setxCoordinate(0);
		
		tableService.update(returnTable, 1);
	}
	
	@Test
	public void updateRestaurantTable_InvalidTableId_ReturnsStatusNotFound() {
		RestaurantTable table = new RestaurantTable(false, 1, 4, 1, 1);
		
		HttpEntity<RestaurantTable> httpEntity = new HttpEntity<RestaurantTable>(table, headers);
		
		ResponseEntity<RestaurantTable> entity = restTemplate
				.exchange("/api/v1/restaurant-table/{id}", HttpMethod.PUT, httpEntity, RestaurantTable.class, 10);
		
		assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
	}
	
	@Test
	public void updateRestaurantTable_InvalidCapacity_ReturnsStatusBadRequest() {
		RestaurantTable table = new RestaurantTable(false, 1, 0, 1, 1);
		
		HttpEntity<RestaurantTable> httpEntity = new HttpEntity<RestaurantTable>(table, headers);
		
		ResponseEntity<RestaurantTable> entity = restTemplate
				.exchange("/api/v1/restaurant-table/{id}", HttpMethod.PUT, httpEntity, RestaurantTable.class, 2);
		
		assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
	}
	
	@Test
	public void updateRestaurantTable_InvalidTableNumber_ReturnsStatusBadRequest() {
		RestaurantTable table = new RestaurantTable(false, null, 0, 1, 1);
		
		HttpEntity<RestaurantTable> httpEntity = new HttpEntity<RestaurantTable>(table, headers);
		
		ResponseEntity<RestaurantTable> entity = restTemplate
				.exchange("/api/v1/restaurant-table/{id}", HttpMethod.PUT, httpEntity, RestaurantTable.class, 2);
		
		assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
	}
	
	@Test
	public void deleteRestaurantTable_ValidId_StatusNO_CONTENT() throws Exception {
		RestaurantTable table = tableService.create(new RestaurantTable(false, 1, 4, 0, 1));
		
		int size = tableService.findAll().size();
		
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		
		ResponseEntity<Void> entity = restTemplate
				.exchange("/api/v1/restaurant-table/{id}", HttpMethod.DELETE, httpEntity, Void.class, table.getId());
		
		assertEquals(HttpStatus.NO_CONTENT, entity.getStatusCode());
		assertEquals(size - 1, tableService.findAll().size());
	}
	
	@Test
	public void deleteRestaurantTable_ValidId_StatusNOT_FOUND() throws Exception {
		int size = tableService.findAll().size();
		
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		
		ResponseEntity<Void> entity = restTemplate
				.exchange("/api/v1/restaurant-table/{id}", HttpMethod.DELETE, httpEntity, Void.class, 10);
		
		assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
		assertEquals(size, tableService.findAll().size());	
	}
}
