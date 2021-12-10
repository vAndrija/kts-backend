package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.cook.CookCreateDto;
import com.kti.restaurant.dto.cook.CookDto;
import com.kti.restaurant.dto.cook.CookUpdateDto;
import com.kti.restaurant.model.Cook;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.CookService;
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
public class CookControllerIntegrationTests {
    private static final String URL_PREFIX = "/api/v1/cook";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CookService cookService;

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
    public void create_UniqueEmail_ValidCook() throws Exception {
        int size = cookService.findAll().size();

        HttpEntity<CookCreateDto> httpEntity = new HttpEntity<>(new CookCreateDto("Aleksa", "Maric",
                "111111", "152487", "aleksamaric6@gmail.com",true), headers);
        ResponseEntity<CookDto> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, CookDto.class);

        CookDto cookDto = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Aleksa", cookDto.getName());
        assertEquals("Maric", cookDto.getLastName());
        assertEquals("111111", cookDto.getPhoneNumber());
        assertEquals("152487", cookDto.getAccountNumber());
        assertEquals("aleksamaric6@gmail.com", cookDto.getEmailAddress());

        List<Cook> cookList = cookService.findAll();
        assertEquals(size + 1, cookList.size());
        assertEquals("aleksamaric6@gmail.com", cookList.get(size).getEmailAddress());

        cookService.delete(cookDto.getId());

    }

    @Test
    public void create_NonUniqueEmail_ThrowsConflictException() {
        HttpEntity<CookCreateDto> httpEntity = new HttpEntity<>(new CookCreateDto("Ana", "Popovic",
                "111111", "152487", "kristinamisic@gmail.com",true), headers);
        ResponseEntity<Cook> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, Cook.class);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void getCooks_CooksList() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<CookDto[]> responseEntity = restTemplate.exchange(URL_PREFIX, HttpMethod.GET, httpEntity,
                CookDto[].class);

        CookDto[] cookDtos = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, cookDtos.length);
        assertEquals("kristinamisic@gmail.com", cookDtos[0].getEmailAddress());
        assertEquals("urosmatic@gmail.com", cookDtos[1].getEmailAddress());
    }

    @Test
    public void getCook_ValidId_ExistingCook() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<CookDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, CookDto.class, 4);

        CookDto cookDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("kristinamisic@gmail.com", cookDto.getEmailAddress());
        assertEquals("kristina", cookDto.getName());
        assertEquals("misic", cookDto.getLastName());
        assertEquals("0697425831", cookDto.getPhoneNumber());
        assertEquals("85315612318963", cookDto.getAccountNumber());

    }

    @Test
    public void getCook_InvalidId_ThrowsMissingEntityException() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<CookDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, CookDto.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void updateCook_ValidIdAndValidJWT_ExistingCook() throws Exception {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("kristinamisic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<CookUpdateDto> httpEntity = new HttpEntity<>(new CookUpdateDto("kristina", "misic",
                "06974258311", "853156123189631",true), httpHeaders);
        ResponseEntity<CookDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                CookDto.class, 4);

        CookDto cookDto = responseEntity1.getBody();
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals("kristina", cookDto.getName());
        assertEquals("misic", cookDto.getLastName());
        assertEquals("06974258311", cookDto.getPhoneNumber());
        assertEquals("853156123189631", cookDto.getAccountNumber());

        Cook cook = cookService.findById(4);
        assertEquals("kristina", cook.getName());
        assertEquals("misic", cook.getLastName());
        assertEquals("06974258311", cook.getPhoneNumber());
        assertEquals("853156123189631", cook.getAccountNumber());

        cook.setLastName("misic");
        cook.setPhoneNumber("0697425831");
        cook.setAccountNumber("85315612318963");

        cookService.update(cook, 4);

    }

    @Test
    public void updateCook_ValidIdAndInvalidJWT_ExistingCook() throws Exception {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("urosmatic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<CookUpdateDto> httpEntity = new HttpEntity<>(new CookUpdateDto("mirko", "savic",
                "152487", "8795613",true), httpHeaders);
        ResponseEntity<CookDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                CookDto.class, 4);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity1.getStatusCode());

    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() {
        HttpEntity<CookUpdateDto> httpEntity = new HttpEntity<>(new CookUpdateDto("ana", "savic",
                "152487", "8795613",true), headers);
        ResponseEntity<CookDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                CookDto.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity1.getStatusCode());
    }

    @Test
    public void delete_ValidId_CookDeleted() throws Exception {
        Cook cook = cookService.create(new Cook("Aleksa", "Maric",
                "111111","aleksamaric5@gmail.com", "152487", true));
        int size = cookService.findAll().size();

        HttpEntity<CookUpdateDto> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<CookDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE, httpEntity,
                CookDto.class, cook.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(size - 1, cookService.findAll().size());
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() throws Exception {
        int size = cookService.findAll().size();

        HttpEntity<CookUpdateDto> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<CookDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE, httpEntity,
                CookDto.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, cookService.findAll().size());
    }
}
