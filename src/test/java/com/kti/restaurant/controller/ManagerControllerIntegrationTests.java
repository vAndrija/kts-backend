package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.admin.AdminCreateDto;
import com.kti.restaurant.dto.admin.AdminDto;
import com.kti.restaurant.dto.admin.AdminUpdateDto;
import com.kti.restaurant.dto.manager.ManagerCreateDto;
import com.kti.restaurant.dto.manager.ManagerDto;
import com.kti.restaurant.dto.manager.ManagerUpdateDto;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.model.Manager;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.AdminService;
import com.kti.restaurant.service.implementation.ManagerService;
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
public class ManagerControllerIntegrationTests {

    private static final String URL_PREFIX = "/api/v1/manager";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ManagerService managerService;

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
    public void create_UniqueEmail_ValidManager() throws Exception {
        int size = managerService.findAll().size();

        HttpEntity<ManagerCreateDto> httpEntity = new HttpEntity<>(new ManagerCreateDto("Aleksa", "Maric",
                "111111", "152487", "aleksamaric@gmail.com"), headers);
        ResponseEntity<ManagerDto> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, ManagerDto.class);

        ManagerDto managerDto = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Aleksa", managerDto.getName());
        assertEquals("Maric", managerDto.getLastName());
        assertEquals("111111", managerDto.getPhoneNumber());
        assertEquals("152487", managerDto.getAccountNumber());
        assertEquals("aleksamaric@gmail.com", managerDto.getEmailAddress());

        List<Manager> managerList = managerService.findAll();
        assertEquals(size + 1, managerList.size());
        assertEquals("aleksamaric@gmail.com", managerList.get(size).getEmailAddress());

        managerService.delete(managerDto.getId());

    }

    @Test
    public void create_NonUniqueEmail_ThrowsConflictException() {
        HttpEntity<ManagerCreateDto> httpEntity = new HttpEntity<>(new ManagerCreateDto("Ana", "Popovic",
                "111111", "152487", "sarajovic@gmail.com"), headers);
        ResponseEntity<Manager> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, Manager.class);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void getManagers_ManagersList() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ManagerDto[]> responseEntity = restTemplate.exchange(URL_PREFIX, HttpMethod.GET, httpEntity,
                ManagerDto[].class);

        ManagerDto[] managerDtos = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, managerDtos.length);
        assertEquals("sarajovic@gmail.com", managerDtos[0].getEmailAddress());
    }

    @Test
    public void getManager_ValidId_ExistingManager() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ManagerDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, ManagerDto.class, 6);

        ManagerDto managerDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("sarajovic@gmail.com", managerDto.getEmailAddress());
        assertEquals("sara", managerDto.getName());
        assertEquals("jovic", managerDto.getLastName());
        assertEquals("0647456821", managerDto.getPhoneNumber());
        assertEquals("78615616918959", managerDto.getAccountNumber());

    }

    @Test
    public void getManager_InvalidId_ThrowsMissingEntityException() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ManagerDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, ManagerDto.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void updateManager_ValidId_ExistingManager() throws Exception {


        HttpEntity<ManagerUpdateDto> httpEntity = new HttpEntity<>(new ManagerUpdateDto("sara", "jovic",
                "06474568211", "786156169189591"), headers);
        ResponseEntity<ManagerDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                ManagerDto.class, 6);

        ManagerDto managerDto = responseEntity1.getBody();
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals("sara", managerDto.getName());
        assertEquals("jovic", managerDto.getLastName());
        assertEquals("06474568211", managerDto.getPhoneNumber());
        assertEquals("786156169189591", managerDto.getAccountNumber());

        Manager manager = managerService.findById(6);
        assertEquals("sara", manager.getName());
        assertEquals("jovic", manager.getLastName());
        assertEquals("06474568211", manager.getPhoneNumber());
        assertEquals("786156169189591", manager.getAccountNumber());

        manager.setPhoneNumber("0647456821");
        manager.setAccountNumber("78615616918959");

        managerService.update(manager, 6);

    }

    @Test
    public void update_InvalidId_ThrowsMissingEntityException() {
        HttpEntity<ManagerUpdateDto> httpEntity = new HttpEntity<>(new ManagerUpdateDto("ana", "savic",
                "152487", "8795613"), headers);
        ResponseEntity<ManagerDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                ManagerDto.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity1.getStatusCode());
    }

    @Test
    public void delete_ValidId_ManagerDeleted() throws Exception {
        Manager manager = managerService.create(new Manager("Aleksa", "Maric",
                "111111", "152487", "aleksamaric@gmail.com"));
        int size = managerService.findAll().size();

        HttpEntity<ManagerUpdateDto> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ManagerDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE, httpEntity,
                ManagerDto.class, manager.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(size - 1, managerService.findAll().size());
    }

    @Test
    public void delete_InvalidId_ThrowsMissingEntityException() throws Exception {
        int size = managerService.findAll().size();

        HttpEntity<ManagerUpdateDto> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<ManagerDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE, httpEntity,
                ManagerDto.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, managerService.findAll().size());
    }
}
