package com.kti.restaurant.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

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
import com.kti.restaurant.dto.tablereservation.TableReservationDto;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.model.TableReservation;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.TableReservationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class TableReservationControllerIntegrationTests {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired
	private TableReservationService reservationService;
		
	private String accessToken;
	private HttpHeaders headers = new HttpHeaders();
	
	@BeforeEach
	public void login() {
		ResponseEntity<UserTokenState> responseEntity = 
				restTemplate.postForEntity("/api/v1/auth/login", 
						new JwtAuthenticationRequest("jovanpetrovic@gmail.com", "123"), 
						UserTokenState.class);

		accessToken = responseEntity.getBody().getAccessToken();
		headers.add("Authorization", "Bearer " + accessToken);
	}
	
	@Test
	public void getTableReservation_ValidId_ReturnsStatusOK() {
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);

		ResponseEntity<TableReservationDto> entity = restTemplate
				.exchange("/api/v1/table-reservations/1", HttpMethod.GET, httpEntity, TableReservationDto.class);
		
		TableReservationDto table = entity.getBody();
		
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(Integer.valueOf(1), table.getId());
	}
	
	@Test
	public void getTableReservations_ReservationsExists_ReturnsStatusOK() {
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		
		ResponseEntity<TableReservationDto[]> entity = restTemplate
				.exchange("/api/v1/table-reservations", HttpMethod.GET, httpEntity, TableReservationDto[].class);
		
		TableReservationDto[] reservations = entity.getBody();
		
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(2, reservations.length);
		
	}
	
	
	@Test
	public void createTableReservation_ValidTableReservation_ReturnsStatusCREATED() throws Exception {
		int size = reservationService.findAll().size();
		
		TableReservationDto reservation = new TableReservationDto("Nesto", 1, LocalDateTime.parse("2021-12-12T14:00"));
		
		HttpEntity<TableReservationDto> httpEntity = new HttpEntity<TableReservationDto>(reservation, headers);
		
		ResponseEntity<TableReservationDto> entity = restTemplate
				.postForEntity("/api/v1/table-reservations", httpEntity, TableReservationDto.class);
		
		TableReservationDto createdReservation = entity.getBody();
		
		assertEquals(HttpStatus.CREATED, entity.getStatusCode());
		assertEquals(size + 1, reservationService.findAll().size());
		assertEquals("Nesto", createdReservation.getName());
		assertEquals(Integer.valueOf(1), createdReservation.getTableId());
		
		reservationService.delete(createdReservation.getId());
		assertEquals(size, reservationService.findAll().size());
	} 
	
	@Test
	public void createTableReservation_InvalidReservation_ReturnsStatusBAD_REQUEST() {
		int size = reservationService.findAll().size();
		
		TableReservationDto reservation = new TableReservationDto("Nesto", 1, LocalDateTime.parse("2021-11-18T16:00"));
		
		HttpEntity<TableReservationDto> httpEntity = new HttpEntity<TableReservationDto>(reservation, headers);
		
		ResponseEntity<TableReservationDto> entity = restTemplate
				.exchange("/api/v1/table-reservations", HttpMethod.POST, httpEntity, TableReservationDto.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
		assertEquals(size, reservationService.findAll().size());
	} 

	
	
	@Test
	public void updateTableReservation_ValidReservation_ReturnsStatusOK() throws Exception {
		TableReservationDto reservationDto = new TableReservationDto(1, "Ime", 1, LocalDateTime.parse("2021-11-21T16:00"));
	
		TableReservation oldReservation = reservationService.findById(1);
		
		HttpEntity<TableReservationDto> httpEntity = new HttpEntity<TableReservationDto>(reservationDto, headers);
		
		ResponseEntity<TableReservationDto> entity = restTemplate
				.exchange("/api/v1/table-reservations/1", HttpMethod.PUT, httpEntity, TableReservationDto.class);
		
		TableReservationDto updatedResevation = entity.getBody();
		
		assertEquals(HttpStatus.OK, entity.getStatusCode());
		assertEquals(LocalDateTime.parse("2021-11-21T16:00"), updatedResevation.getDurationStart());
		
		reservationService.update(oldReservation, 1);
	} 
	
	
	@Test
	public void updateTableReservation_InvalidReservation_ReturnsStatusBAD_REQUEST() {
		TableReservationDto reservationDto = new TableReservationDto(1, "Ime", 1, LocalDateTime.parse("2021-11-18T16:00"));
		
		
		HttpEntity<TableReservationDto> httpEntity = new HttpEntity<TableReservationDto>(reservationDto, headers);
		
		ResponseEntity<TableReservationDto> entity = restTemplate
				.exchange("/api/v1/table-reservations/1", HttpMethod.PUT, httpEntity, TableReservationDto.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
	}   
	
	
	@Test
	public void deleteTableReservation_ValidId_ReturnsStatusNO_CONTENT() throws Exception {
		TableReservation reservation = new TableReservation("Novo ime", LocalDateTime.parse("2021-12-20T12:00"), new RestaurantTable(false, 1, 4, 0, 1));
		TableReservation createdReservation = reservationService.create(reservation);
		
		int size = reservationService.findAll().size();
		
		HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
		ResponseEntity<Void> entity = restTemplate
				.exchange("/api/v1/table-reservations/{id}", HttpMethod.DELETE, httpEntity, Void.class, createdReservation.getId());
		
		assertEquals(HttpStatus.NO_CONTENT, entity.getStatusCode());
		assertEquals(size-1, reservationService.findAll().size());
		
	} 
	
	@Test
    public void delete_InvalidId_ReturnsNOT_FOUND() throws Exception {
        int size = reservationService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/v1/table-resevations/{id}", HttpMethod.DELETE, httpEntity,
                Object.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, reservationService.findAll().size());
    }
}
