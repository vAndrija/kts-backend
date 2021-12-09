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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MenuControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MenuService menuService;

    private String accessToken;

    @BeforeEach
    public void login() {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("sarajovic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
    }

    @Test
    public void createMenu_ValidMenu_CreatedMenu() throws Exception {
        int size = menuService.findAll().size();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
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
    public void getMenuById_ValidMenuId_Menu() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
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
    public void getMenuById_InvalidMenu() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        HttpEntity<Object> httpEntity = new HttpEntity<Object>(headers);
        ResponseEntity<Menu> responseEntity =
                restTemplate.exchange("/api/v1/menu/{id}", HttpMethod.GET, httpEntity, Menu.class, 3);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}