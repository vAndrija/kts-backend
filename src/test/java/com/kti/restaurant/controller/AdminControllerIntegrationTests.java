package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.admin.AdminCreateDto;
import com.kti.restaurant.dto.admin.AdminDto;
import com.kti.restaurant.dto.admin.AdminUpdateDto;
import com.kti.restaurant.model.Admin;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.AdminService;
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
public class AdminControllerIntegrationTests {

    private static final String URL_PREFIX = "/api/v1/admin";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AdminService adminService;

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
    public void create_UniqueEmail_ReturnsCreated() throws Exception {
        int size = adminService.findAll().size();

        HttpEntity<AdminCreateDto> httpEntity = new HttpEntity<>(new AdminCreateDto("Aleksa", "Maric",
                "111111", "152487", "aleksamaric2@gmail.com"), headers);
        ResponseEntity<AdminDto> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, AdminDto.class);

        AdminDto adminDto = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Aleksa", adminDto.getName());
        assertEquals("Maric", adminDto.getLastName());
        assertEquals("111111", adminDto.getPhoneNumber());
        assertEquals("152487", adminDto.getAccountNumber());
        assertEquals("aleksamaric2@gmail.com", adminDto.getEmailAddress());

        List<Admin> adminList = adminService.findAll();
        assertEquals(size + 1, adminList.size());
        assertEquals("aleksamaric2@gmail.com", adminList.get(size).getEmailAddress());

        adminService.delete(adminDto.getId());

    }

    @Test
    public void create_NonUniqueEmail_ReturnsConflict() {
        int size = adminService.findAll().size();
        HttpEntity<AdminCreateDto> httpEntity = new HttpEntity<>(new AdminCreateDto("Ana", "Popovic",
                "111111", "152487", "anapopovic@gmail.com"), headers);
        ResponseEntity<Admin> responseEntity = restTemplate.postForEntity(URL_PREFIX, httpEntity, Admin.class);
        int sizeAfter = adminService.findAll().size();
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals(size,sizeAfter);
    }

    @Test
    public void getAdmins_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<AdminDto[]> responseEntity = restTemplate.exchange(URL_PREFIX, HttpMethod.GET, httpEntity,
                AdminDto[].class);

        AdminDto[] adminDtos = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(5, adminDtos.length);
        assertEquals("mirkomiric@gmail.com", adminDtos[0].getEmailAddress());
        assertEquals("mirkomiric1@gmail.com", adminDtos[1].getEmailAddress());
    }

    @Test
    public void getAdmin_ValidId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<AdminDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, AdminDto.class, 1);

        AdminDto adminDto = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("mirkomiric@gmail.com", adminDto.getEmailAddress());
        assertEquals("mirko", adminDto.getName());
        assertEquals("miric", adminDto.getLastName());
        assertEquals("0608963214", adminDto.getPhoneNumber());
        assertEquals("2131231231231", adminDto.getAccountNumber());

    }

    @Test
    public void getAdmin_InvalidId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<AdminDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.GET,
                httpEntity, AdminDto.class, 2);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void updateAdmin_ValidId_ReturnsOk() throws Exception {
        HttpEntity<AdminUpdateDto> httpEntity = new HttpEntity<>(new AdminUpdateDto("mirko", "savic",
                "152487", "8795613"), headers);
        ResponseEntity<AdminDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                AdminDto.class, 1);

        AdminDto adminDto = responseEntity1.getBody();
        assertEquals(HttpStatus.OK, responseEntity1.getStatusCode());
        assertEquals("mirko", adminDto.getName());
        assertEquals("savic", adminDto.getLastName());
        assertEquals("152487", adminDto.getPhoneNumber());
        assertEquals("8795613", adminDto.getAccountNumber());

        Admin admin = adminService.findById(1);
        assertEquals("mirko", admin.getName());
        assertEquals("savic", admin.getLastName());
        assertEquals("152487", admin.getPhoneNumber());
        assertEquals("8795613", admin.getAccountNumber());

        admin.setLastName("miric");
        admin.setPhoneNumber("0608963214");
        admin.setAccountNumber("2131231231231");

        adminService.update(admin, 1);

    }

    @Test
    public void update_InvalidId_ReturnsNotFound() {
        HttpEntity<AdminUpdateDto> httpEntity = new HttpEntity<>(new AdminUpdateDto("ana", "savic",
                "152487", "8795613"), headers);
        ResponseEntity<AdminDto> responseEntity1 = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.PUT, httpEntity,
                AdminDto.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity1.getStatusCode());
    }

    @Test
    public void delete_ValidId_ReturnsNoContent() throws Exception {
        Admin admin = adminService.create(new Admin("Aleksa", "Maric",
                "111111", "aleksamaric1@gmail.com", "152487"));
        int size = adminService.findAll().size();

        HttpEntity<AdminUpdateDto> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<AdminDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE, httpEntity,
                AdminDto.class, admin.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(size - 1, adminService.findAll().size());
    }

    @Test
    public void delete_InvalidId_ReturnsNotFound() throws Exception {
        int size = adminService.findAll().size();

        HttpEntity<AdminUpdateDto> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<AdminDto> responseEntity = restTemplate.exchange(URL_PREFIX + "/{id}", HttpMethod.DELETE, httpEntity,
                AdminDto.class, 100);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, adminService.findAll().size());
    }
}
