package com.kti.restaurant.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
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
import com.kti.restaurant.dto.tablereservation.TableReservationDto;
import com.kti.restaurant.model.RestaurantTable;
import com.kti.restaurant.model.TableReservation;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.RestaurantTableService;
import com.kti.restaurant.service.implementation.TableReservationService;
import com.kti.restaurant.utils.GsonUtils;
import com.kti.restaurant.utils.RestResponsePage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class TableReservationControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TableReservationService reservationService;

    @Autowired
    private RestaurantTableService tableService;

    private String accessToken;
    private HttpHeaders headers;

    @BeforeEach
    public void login() {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("jovanpetrovic@gmail.com", "123"),
                        UserTokenState.class);

        accessToken = responseEntity.getBody().getAccessToken();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + accessToken);
    }

    @Test
    public void getTableReservation_ValidId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);

        ResponseEntity<TableReservationDto> entity = restTemplate
                .exchange("/api/v1/table-reservations/1", HttpMethod.GET, httpEntity, TableReservationDto.class);

        TableReservationDto table = entity.getBody();

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(Integer.valueOf(1), table.getId());
    }

    @Test
    public void getTableReservations_InvalidId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);

        ResponseEntity<TableReservationDto> entity = restTemplate
                .exchange("/api/v1/table-reservations/3", HttpMethod.GET, httpEntity, TableReservationDto.class);

        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());

    }


    @Test
    public void getTableReservations_ReservationsExists_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);

        ParameterizedTypeReference<RestResponsePage<TableReservationDto>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<RestResponsePage<TableReservationDto>> entity = restTemplate
                .exchange("/api/v1/table-reservations/?page={page}&size={size}", HttpMethod.GET, httpEntity, responseType, 0, 8);

        List<TableReservationDto> reservations = entity.getBody().getContent();

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(2, reservations.size());

    }

    @Test
    public void createTableReservation_ValidTableReservation_ReturnsCreated() throws Exception {
        int size = reservationService.findAll().size();

        TableReservationDto reservation = new TableReservationDto("Nesto", 1, LocalDateTime.parse("2021-12-12T14:00"),  LocalDateTime.parse("2021-12-12T15:00"));


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
    public void createTableReservation_InvalidReservation_ReturnsBadRequest() {
        int size = reservationService.findAll().size();

        TableReservationDto reservation = new TableReservationDto("Nesto", 1, LocalDateTime.parse("2021-11-18T16:00"),  LocalDateTime.parse("2021-11-18T17:22"));

        HttpEntity<TableReservationDto> httpEntity = new HttpEntity<TableReservationDto>(reservation, headers);

        ResponseEntity<TableReservationDto> entity = restTemplate
                .exchange("/api/v1/table-reservations", HttpMethod.POST, httpEntity, TableReservationDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
        assertEquals(size, reservationService.findAll().size());
    }

    @Test
    public void createTableReservation_NonexistentTableId_ReturnsNotFound() {
        int size = reservationService.findAll().size();

        TableReservationDto reservation = new TableReservationDto("Nesto", -10, LocalDateTime.parse("2021-11-18T16:00"),  LocalDateTime.parse("2021-11-18T17:22"));

        HttpEntity<TableReservationDto> httpEntity = new HttpEntity<TableReservationDto>(reservation, headers);

        ResponseEntity<TableReservationDto> entity = restTemplate
                .exchange("/api/v1/table-reservations", HttpMethod.POST, httpEntity, TableReservationDto.class);

        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
        assertEquals(size, reservationService.findAll().size());
    }

    @ParameterizedTest
    @MethodSource("provideTableReservationDtoForCreate")
    public void createTableReservation_InvalidParameters_ReturnsBadRequest(String name, Integer id, LocalDateTime startDate, LocalDateTime endDate, String bodyParameter, String errorMessage) {
        int size = reservationService.findAll().size();

        TableReservationDto reservation = new TableReservationDto(name, id, startDate, endDate);

        HttpEntity<String> httpEntity = new HttpEntity<String>(GsonUtils.ToJson(reservation), headers);

        ParameterizedTypeReference<HashMap<String, String>> responseType = new ParameterizedTypeReference<HashMap<String, String>>() {
        };

        ResponseEntity<HashMap<String, String>> entity = restTemplate
                .exchange("/api/v1/table-reservations", HttpMethod.POST, httpEntity, responseType);

        HashMap<String, String> body = entity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
        assertEquals(errorMessage, body.get(bodyParameter));
        assertEquals(size, reservationService.findAll().size());
    }

    private static Stream<Arguments> provideTableReservationDtoForCreate() {

        return Stream.of(
                Arguments.of("Nesto", 1, null, LocalDateTime.parse("2021-10-10T22:22"), "durationStart", "Start date should not be null or empty"),
                Arguments.of("Nesto", null, LocalDateTime.parse("2021-11-18T16:00"), LocalDateTime.parse("2021-10-10T22:22") ,"tableId", "Table id should not be null"),
                Arguments.of(null, 1, LocalDateTime.parse("2021-11-18T16:00"), LocalDateTime.parse("2021-10-10T22:22"), "name", "Name should not be null or empty")

        );
    }

    @Test
    public void updateTableReservation_ValidReservation_ReturnsOk() throws Exception {
        TableReservationDto reservationDto = new TableReservationDto(1, "Ime", 1, LocalDateTime.parse("2021-11-21T16:00"),  LocalDateTime.parse("2021-11-10T17:22"));

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
    public void updateTableReservation_InvalidReservation_ReturnsBadRequest() {
        TableReservationDto reservationDto = new TableReservationDto(2, "Ime", 1, LocalDateTime.parse("2021-11-18T17:22"), LocalDateTime.parse("2021-11-18T22:22"));

        HttpEntity<TableReservationDto> httpEntity = new HttpEntity<TableReservationDto>(reservationDto, headers);

        ResponseEntity<TableReservationDto> entity = restTemplate
                .exchange("/api/v1/table-reservations/2", HttpMethod.PUT, httpEntity, TableReservationDto.class);

        assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    public void updateTableReservation_NonexistentTableId_ReturnsNotFound() {
        int size = reservationService.findAll().size();

        TableReservationDto reservation = new TableReservationDto("Nesto", -10, LocalDateTime.parse("2021-11-18T16:00"),  LocalDateTime.parse("2021-11-18T17:22"));

        HttpEntity<TableReservationDto> httpEntity = new HttpEntity<TableReservationDto>(reservation, headers);

        ResponseEntity<TableReservationDto> entity = restTemplate
                .exchange("/api/v1/table-reservations/{id}", HttpMethod.PUT, httpEntity, TableReservationDto.class, 1);

        assertEquals(HttpStatus.NOT_FOUND, entity.getStatusCode());
        assertEquals(size, reservationService.findAll().size());
    }

    @ParameterizedTest
    @MethodSource("provideTableReservationDtoForUpdate")
    public void updateTableReservation_InvalidParameters_ReturnsBadRequest(String name, Integer id, LocalDateTime startDate, LocalDateTime endDate, String bodyParameter, String errorMessage) {
        int size = reservationService.findAll().size();

        TableReservationDto reservation = new TableReservationDto(name, id, startDate, endDate);

        HttpEntity<String> httpEntity = new HttpEntity<String>(GsonUtils.ToJson(reservation), headers);

        ParameterizedTypeReference<HashMap<String, String>> responseType = new ParameterizedTypeReference<HashMap<String, String>>() {
        };

        ResponseEntity<HashMap<String, String>> entity = restTemplate
                .exchange("/api/v1/table-reservations/{1}", HttpMethod.PUT, httpEntity, responseType, 1);

        HashMap<String, String> body = entity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
        assertEquals(errorMessage, body.get(bodyParameter));
        assertEquals(size, reservationService.findAll().size());
    }

    private static Stream<Arguments> provideTableReservationDtoForUpdate() {

        return Stream.of(
                Arguments.of(null, 1, LocalDateTime.parse("2021-11-18T16:00"), LocalDateTime.parse("2021-11-10T17:22"), "name", "Name should not be null or empty"),
                Arguments.of("Nesto", 1, null, LocalDateTime.parse("2021-10-10T22:22"), "durationStart", "Start date should not be null or empty"),
                Arguments.of("Nesto", null, LocalDateTime.parse("2021-11-18T16:00"), LocalDateTime.parse("2021-11-18T17:22"), "tableId", "Table id should not be null")
        );
    }

    @Test
    public void deleteTableReservation_ValidId_ReturnsNoContent() throws Exception {
        RestaurantTable table = tableService.findById(3);

        TableReservation reservation = new TableReservation("Novo ime", LocalDateTime.parse("2021-12-20T12:00"),  LocalDateTime.parse("2021-12-20T14:22"),table);
        TableReservation createdReservation = reservationService.create(reservation);

        int size = reservationService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        ResponseEntity<Void> entity = restTemplate
                .exchange("/api/v1/table-reservations/{id}", HttpMethod.DELETE, httpEntity, Void.class, createdReservation.getId());

        assertEquals(HttpStatus.NO_CONTENT, entity.getStatusCode());
        assertEquals(size - 1, reservationService.findAll().size());

    }

    @Test
    public void delete_InvalidId_ReturnsNotFound() {
        int size = reservationService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Object> responseEntity = restTemplate.exchange("/api/v1/table-resevations/{id}", HttpMethod.DELETE, httpEntity,
                Object.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, reservationService.findAll().size());
    }
}
