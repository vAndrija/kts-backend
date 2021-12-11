package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.menu.MenuDto;
import com.kti.restaurant.model.Menu;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MenuControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MenuService menuService;

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
    public void createMenu_ValidMenu_ReturnsCreated() throws Exception {
        int size = menuService.findAll().size();

        HttpEntity<MenuDto> httpEntity = new HttpEntity<>(new MenuDto("Glavni meni", LocalDateTime.parse("2021-11-11T13:00"),
                LocalDateTime.parse("2022-05-11T13:00")), headers);
        ResponseEntity<Menu> responseEntity =
                restTemplate.postForEntity("/api/v1/menu", httpEntity,
                        Menu.class);

        Menu menu = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Glavni meni", menu.getName());
        assertEquals(LocalDateTime.parse("2021-11-11T13:00"), menu.getDurationStart());
        assertEquals(LocalDateTime.parse("2022-05-11T13:00"), menu.getDurationEnd());

        List<Menu> menus = menuService.findAll();
        assertEquals(size+1, menus.size());
        assertEquals(LocalDateTime.parse("2021-11-11T13:00"), menus.get(size).getDurationStart());

        menuService.delete(menu.getId());
    }

    @Test
    public void createMenu_InvalidMenuName_ReturnsBadRequest() throws Exception {
        int size = menuService.findAll().size();

        HttpEntity<MenuDto> httpEntity = new HttpEntity<>(new MenuDto("", LocalDateTime.parse("2021-11-11T13:00"),
                LocalDateTime.parse("2022-05-11T13:00")), headers);
        ResponseEntity<Menu> responseEntity =
                restTemplate.postForEntity("/api/v1/menu", httpEntity,
                        Menu.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(size, menuService.findAll().size());
    }

    @Test
    public void createMenu_InvalidMenuStartDateEndDate_ReturnsBadRequest() throws Exception {
        int size = menuService.findAll().size();

        HttpEntity<MenuDto> httpEntity = new HttpEntity<>(new MenuDto("Glavni", null,
                null), headers);
        ResponseEntity<Menu> responseEntity =
                restTemplate.postForEntity("/api/v1/menu", httpEntity,
                        Menu.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(size, menuService.findAll().size());
    }

    @Test
    public void getMenuById_ValidMenuId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        ResponseEntity<Menu> responseEntity =
                restTemplate.exchange("/api/v1/menu/{id}", HttpMethod.GET, httpEntity, Menu.class, 1);

        Menu menu = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("standardni", menu.getName());
        assertEquals(LocalDateTime.parse("2021-11-18T08:00"), menu.getDurationStart());
        assertEquals(LocalDateTime.parse("2022-11-18T08:00"), menu.getDurationEnd());
    }

    @Test
    public void getMenus_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Menu[]> responseEntity = restTemplate.exchange("/api/v1/menu", HttpMethod.GET, httpEntity, Menu[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, List.of(responseEntity.getBody()).size());
    }

    @Test
    public void getMenuById_InvalidMenu_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        ResponseEntity<Menu> responseEntity =
                restTemplate.exchange("/api/v1/menu/{id}", HttpMethod.GET, httpEntity, Menu.class, 3);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updateMenu_ValidMenu_ReturnsOk() throws Exception {
        HttpEntity<MenuDto> httpEntity = new HttpEntity<MenuDto>(new MenuDto("Glavni", LocalDateTime.parse("2021-11-18T08:00"),
                LocalDateTime.parse("2022-11-18T08:00")), headers);
        ResponseEntity<Menu> responseEntity =
                restTemplate.exchange("/api/v1/menu/{id}", HttpMethod.PUT, httpEntity,
                        Menu.class, 1);

        Menu menu = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Glavni", menu.getName());

        Menu updatedMenu = menuService.findById(1);
        assertEquals("Glavni", updatedMenu.getName());

        updatedMenu.setName("standardni");
        menuService.update(updatedMenu, 1);
    }

    @Test
    public void updateMenu_InvalidMenuId_ReturnsNotFoundStatus() {
        HttpEntity<MenuDto> httpEntity = new HttpEntity<MenuDto>(new MenuDto("Glavni", LocalDateTime.parse("2021-11-18T08:00"),
                LocalDateTime.parse("2022-11-18T08:00")), headers);
        ResponseEntity<Menu> responseEntity =
                restTemplate.exchange("/api/v1/menu/{id}", HttpMethod.PUT, httpEntity,
                        Menu.class, 3);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updateMenu_InvalidMenuName_ReturnsBadRequest() {
        HttpEntity<MenuDto> httpEntity = new HttpEntity<MenuDto>(new MenuDto("", LocalDateTime.parse("2021-11-18T08:00"),
                LocalDateTime.parse("2022-11-18T08:00")), headers);
        ResponseEntity<Menu> responseEntity =
                restTemplate.exchange("/api/v1/menu/{id}", HttpMethod.PUT, httpEntity,
                        Menu.class, 2);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void updateMenu_InvalidMenuStartDateEndDate_ReturnsBadRequest() {
        HttpEntity<MenuDto> httpEntity = new HttpEntity<MenuDto>(new MenuDto("Glavni", null,
                null), headers);
        ResponseEntity<Menu> responseEntity =
                restTemplate.exchange("/api/v1/menu/{id}", HttpMethod.PUT, httpEntity,
                        Menu.class, 2);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void deleteMenu_ValidMenuId_ReturnsNoContent() throws Exception {
        Menu menu = menuService.create(new Menu("Glavni", LocalDateTime.parse("2021-11-08T13:00"),
                LocalDateTime.parse("2022-11-05T13:00")));

        int size = menuService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Menu> responseEntity = restTemplate.exchange("/api/v1/menu/{id}", HttpMethod.DELETE, httpEntity,
                Menu.class, menu.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(size - 1, menuService.findAll().size());
    }

    @Test
    public void deleteMenu_InvalidMenuId_ReturnsNotFoundStatus() {
        int size = menuService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<Menu> responseEntity = restTemplate.exchange("/api/v1/menu/{id}", HttpMethod.DELETE, httpEntity,
                Menu.class, 10);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(size, menuService.findAll().size());
    }
}

