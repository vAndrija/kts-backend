package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.service.implementation.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

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
}
