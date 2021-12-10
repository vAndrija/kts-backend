package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.bartender.BartenderCreateDto;
import com.kti.restaurant.dto.bartender.BartenderDto;
import com.kti.restaurant.dto.bartender.BartenderUpdateDto;
import com.kti.restaurant.model.Bartender;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.BartenderService;
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
public class BartenderControllerIntegrationTests {
    private static final String URL_PREFIX = "/api/v1/bartender";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BartenderService bartenderService;

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
    public void create_UniqueEmail_ValidBartender() throws Exception {
        int size = bartenderService.findAll().size();

        HttpEntity<BartenderCreateDto> httpEntity = new HttpEntity<>(new BartenderCreateDto("Aleksa", "Maric",
                "111111", "152487", "aleksamaric3@gmail.com", true), headers);
        ResponseEntity<BartenderDto> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, BartenderDto.class);

        BartenderDto bartenderDto = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Aleksa", bartenderDto.getName());
        assertEquals("Maric", bartenderDto.getLastName());
        assertEquals("111111", bartenderDto.getPhoneNumber());
        assertEquals("152487", bartenderDto.getAccountNumber());
        assertEquals("aleksamaric3@gmail.com", bartenderDto.getEmailAddress());

        List<Bartender> bartenderList = bartenderService.findAll();
        assertEquals(size + 1, bartenderList.size());
        assertEquals("aleksamaric3@gmail.com", bartenderList.get(size).getEmailAddress());

        bartenderService.delete(bartenderDto.getId());

    }

    @Test
    public void create_NonUniqueEmail_ThrowsConflictException() {
        HttpEntity<BartenderCreateDto> httpEntity = new HttpEntity<>(new BartenderCreateDto("Ana", "Popovic",
                "111111", "152487", "lukaperic@gmail.com", true), headers);
        ResponseEntity<Bartender> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, Bartender.class);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void getBartenders_BartendersList() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<BartenderDto[]> responseEntity = restTemplate.exchange(URL_PREFIX, HttpMethod.GET, httpEntity,
                BartenderDto[].class);

        BartenderDto[] bartenderDtos = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, bartenderDtos.length);
        assertEquals("lukaperic@gmail.com", bartenderDtos[0].getEmailAddress());
        assertEquals("milossaric@gmail.com", bartenderDtos[1].getEmailAddress());
    }

    @Test
    public void getBartender_ValidId_ExistingBartender() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<BartenderDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, BartenderDto.class, 2);

        BartenderDto bartenderDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("lukaperic@gmail.com", bartenderDto.getEmailAddress());
        assertEquals("luka", bartenderDto.getName());
        assertEquals("peric", bartenderDto.getLastName());
        assertEquals("0632589641", bartenderDto.getPhoneNumber());
        assertEquals("6332238931255", bartenderDto.getAccountNumber());

    }

    @Test
    public void getBartender_InvalidId_ThrowsMissingEntityException() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<BartenderDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, BartenderDto.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void updateBartender_ValidIdAndValidJWT_ExistingBartender() throws Exception {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("lukaperic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<BartenderUpdateDto> httpEntity = new HttpEntity<>(new BartenderUpdateDto("luka", "peric",
                "06325896411", "63322389312551", true), httpHeaders);
        ResponseEntity<BartenderDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                BartenderDto.class, 2);

        BartenderDto bartenderDto = responseEntity1.getBody();
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals("luka", bartenderDto.getName());
        assertEquals("peric", bartenderDto.getLastName());
        assertEquals("06325896411", bartenderDto.getPhoneNumber());
        assertEquals("63322389312551", bartenderDto.getAccountNumber());

        Bartender bartender = bartenderService.findById(2);
        assertEquals("luka", bartender.getName());
        assertEquals("peric", bartender.getLastName());
        assertEquals("06325896411", bartender.getPhoneNumber());
        assertEquals("63322389312551", bartender.getAccountNumber());

        bartender.setLastName("peric");
        bartender.setPhoneNumber("0632589641");
        bartender.setAccountNumber("6332238931255");

        bartenderService.update(bartender, 2);

    }

    @Test
    public void updateBartender_ValidIdAndInvalidJWT_ExistingBartender() throws Exception {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("milossaric@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        HttpEntity<BartenderUpdateDto> httpEntity = new HttpEntity<>(new BartenderUpdateDto("mirko", "savic",
                "152487", "8795613", true), httpHeaders);
        ResponseEntity<BartenderDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                BartenderDto.class, 2);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity1.getStatusCode());

    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() {
        HttpEntity<BartenderUpdateDto> httpEntity = new HttpEntity<>(new BartenderUpdateDto("ana", "savic",
                "152487", "8795613", true), headers);
        ResponseEntity<BartenderDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                BartenderDto.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity1.getStatusCode());
    }

    @Test
    public void delete_ValidId_BartenderDeleted() throws Exception {
        Bartender bartender = bartenderService.create(new Bartender("Aleksa", "Maric",
                "111111", "aleksamaric4@gmail.com", "152487", true));
        int size = bartenderService.findAll().size();

        HttpEntity<BartenderUpdateDto> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<BartenderDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE, httpEntity,
                BartenderDto.class, bartender.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(size - 1, bartenderService.findAll().size());
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() throws Exception {
        int size = bartenderService.findAll().size();

        HttpEntity<BartenderUpdateDto> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<BartenderDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE, httpEntity,
                BartenderDto.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, bartenderService.findAll().size());
    }
}
