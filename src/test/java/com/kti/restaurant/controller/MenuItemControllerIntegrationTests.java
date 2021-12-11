package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.menuitem.MenuItemDto;
import com.kti.restaurant.dto.menuitem.UpdateMenuItemDto;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import com.kti.restaurant.service.implementation.MenuItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MenuItemControllerIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MenuItemService menuItemService;

    private String accessToken;

    private HttpHeaders headers;

    @BeforeEach
    public void login() {
        ResponseEntity<UserTokenState> responseEntity =
                restTemplate.postForEntity("/api/v1/auth/login",
                        new JwtAuthenticationRequest("kristinamisic@gmail.com", "123"),
                        UserTokenState.class);
        accessToken = responseEntity.getBody().getAccessToken();
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
    }

    @Test
    public void createMenuItem_ValidMenuItem_ReturnsOk() throws Exception {
        int size = menuItemService.findAll().size();

        HttpEntity<MenuItemDto> httpEntity = new HttpEntity<>(new MenuItemDto("Sarma", "Jelo od mlevenog mesa i kiselog kupusa", MenuItemType.FOOD,
                MenuItemCategory.MAIN_COURSE, 10), headers);
        ResponseEntity<MenuItem> responseEntity = restTemplate.postForEntity("/api/v1/menu-items", httpEntity, MenuItem.class);

        MenuItem menuItem = responseEntity.getBody();

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Sarma", menuItem.getName());
        assertEquals("Jelo od mlevenog mesa i kiselog kupusa", menuItem.getDescription());
        assertEquals(MenuItemType.FOOD, menuItem.getType());
        assertEquals(MenuItemCategory.MAIN_COURSE, menuItem.getCategory());
        assertEquals(10, menuItem.getPreparationTime());

        List<MenuItem> menuItems = menuItemService.findAll();
        assertEquals(size + 1, menuItems.size());
        assertEquals("Sarma", menuItems.get(size).getName());

        menuItemService.delete(menuItem.getId());
    }

    @Test
    public void createMenuItem_InvalidMenuItemName_RetrunsBadRequest() {
        int size = menuItemService.findAll().size();

        HttpEntity<MenuItemDto> httpEntity = new HttpEntity<>(new MenuItemDto("", "Jelo od mlevenog mesa i kiselog kupusa", MenuItemType.FOOD,
                MenuItemCategory.MAIN_COURSE, 10), headers);
        ResponseEntity<MenuItem> responseEntity = restTemplate.postForEntity("/api/v1/menu-items", httpEntity, MenuItem.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(size, menuItemService.findAll().size());
    }

    @Test
    public void getMenuItemById_ValidMenuItemId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<MenuItem> responseEntity = restTemplate.exchange("/api/v1/menu-items/{id}", HttpMethod.GET, httpEntity, MenuItem.class, 1);

        MenuItem menuItem = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("coca cola", menuItem.getName());
        assertEquals("bezalkoholno gazirano pice", menuItem.getDescription());
        assertEquals(MenuItemType.DRINK, menuItem.getType());
        assertEquals(MenuItemCategory.NON_ALCOHOLIC, menuItem.getCategory());
        assertEquals(2, menuItem.getPreparationTime());
    }

    @Test
    public void getMenuItemById_InvalidMenuItem_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<MenuItem> responseEntity = restTemplate.exchange("/api/v1/menu-items/{id}", HttpMethod.GET, httpEntity, MenuItem.class, 20);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getMenuItems_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<MenuItem[]> responseEntity = restTemplate.exchange("/api/v1/menu-items", HttpMethod.GET, httpEntity, MenuItem[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(14, List.of(responseEntity.getBody()).size());
    }

    @Test
    public void updateMenuItem_ValidMenuItemId_ReturnsOk() throws Exception {
        HttpEntity<UpdateMenuItemDto> httpEntity = new HttpEntity<>(new UpdateMenuItemDto("Coca cola", "bezalkoholno gazirano pice", MenuItemType.DRINK,
                MenuItemCategory.NON_ALCOHOLIC, 1, true, 2), headers);
        ResponseEntity<MenuItem> responseEntity = restTemplate.exchange("/api/v1/menu-items/{id}", HttpMethod.PUT, httpEntity, MenuItem.class, 1);

        MenuItem menuItem = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Coca cola", menuItem.getName());

        MenuItem updatedMenuItem = menuItemService.findById(menuItem.getId());
        assertEquals("Coca cola", updatedMenuItem.getName());

        updatedMenuItem.setName("coca cola");
        menuItemService.update(updatedMenuItem, updatedMenuItem.getId());
    }

    @Test
    public void updateMenuItem_InvalidMenuItemId_ReturnsNotFound() {
        HttpEntity<UpdateMenuItemDto> httpEntity = new HttpEntity<>(new UpdateMenuItemDto("Coca cola", "bezalkoholno gazirano pice", MenuItemType.DRINK,
                MenuItemCategory.NON_ALCOHOLIC, 1, true, 2), headers);
        ResponseEntity<MenuItem> responseEntity = restTemplate.exchange("/api/v1/menu-items/{id}", HttpMethod.PUT, httpEntity, MenuItem.class, 20);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updateMenuItem_InvalidMenuItemName_ReturnsBadRequest() {
        HttpEntity<UpdateMenuItemDto> httpEntity = new HttpEntity<>(new UpdateMenuItemDto("", "bezalkoholno gazirano pice", MenuItemType.DRINK,
                MenuItemCategory.NON_ALCOHOLIC, 1, true, 2), headers);
        ResponseEntity<MenuItem> responseEntity = restTemplate.exchange("/api/v1/menu-items/{id}", HttpMethod.PUT, httpEntity, MenuItem.class, 1);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void deleteMenuItem_ValidMenuItemId_ReturnsOk() throws Exception {
        MenuItem menuItem = menuItemService.create(new MenuItem("Sarma", "Jelo od mlevenog mesa i kiselog kupusa", MenuItemCategory.MAIN_COURSE,
                MenuItemType.FOOD, 10));
        int size = menuItemService.findAll().size();

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<MenuItem> responseEntity = restTemplate.exchange("/api/v1/menu-items/{id}", HttpMethod.DELETE, httpEntity, MenuItem.class, menuItem.getId());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(size - 1, menuItemService.findAll().size());
    }

    @Test
    public void deleteMenuItem_InvalidMenuItemId_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<MenuItem> responseEntity = restTemplate.exchange("/api/v1/menu-items/{id}", HttpMethod.DELETE, httpEntity, MenuItem.class, 20);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void searchMenuItems_ValidSearchParameter_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<MenuItem[]> responseEntity = restTemplate.exchange("/api/v1/menu-items/search/{search}", HttpMethod.GET, httpEntity,
                MenuItem[].class, "bezalkoholno");

        List<MenuItem> menuItems = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, menuItems.size());
        assertTrue(menuItems.get(0).getDescription().contains("bezalkoholno"));
        assertTrue(menuItems.get(1).getDescription().contains("bezalkoholno"));
    }

    @Test
    public void searchMenuItems_InvalidSearchParameter_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<MenuItem[]> responseEntity = restTemplate.exchange("/api/v1/menu-items/search/{search}", HttpMethod.GET, httpEntity,
                MenuItem[].class, "abcd");

        List<MenuItem> menuItems = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, menuItems.size());
    }

    @Test
    public void filterMenuItems_ValidFilterParameter_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<MenuItem[]> responseEntity = restTemplate.exchange("/api/v1/menu-items/filter/{filter}", HttpMethod.GET, httpEntity,
                MenuItem[].class, "Dezert");

        List<MenuItem> menuItems = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(4, menuItems.size());
        assertEquals(MenuItemCategory.DESSERT, menuItems.get(0).getCategory());
        assertEquals(MenuItemCategory.DESSERT, menuItems.get(1).getCategory());
        assertEquals(MenuItemCategory.DESSERT, menuItems.get(2).getCategory());
        assertEquals(MenuItemCategory.DESSERT, menuItems.get(3).getCategory());
    }

    @Test
    public void filterMenuItems_InvalidFilterParameter_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<MenuItem[]> responseEntity = restTemplate.exchange("/api/v1/menu-items/filter/{filter}", HttpMethod.GET, httpEntity,
                MenuItem[].class, "abcd");

        List<MenuItem> menuItems = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, menuItems.size());
    }
}
