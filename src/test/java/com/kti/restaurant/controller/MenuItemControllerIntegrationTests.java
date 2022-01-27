package com.kti.restaurant.controller;

import com.kti.restaurant.dto.JwtAuthenticationRequest;
import com.kti.restaurant.dto.menuitem.CreateMenuItemDto;
import com.kti.restaurant.dto.menuitem.MenuItemDto;
import com.kti.restaurant.dto.menuitem.UpdateMenuItemDto;
import com.kti.restaurant.exception.MissingEntityException;
import com.kti.restaurant.model.MenuItem;
import com.kti.restaurant.model.UserTokenState;
import com.kti.restaurant.model.enums.MenuItemCategory;
import com.kti.restaurant.model.enums.MenuItemType;
import com.kti.restaurant.service.implementation.MenuItemService;
import com.kti.restaurant.utils.RestResponsePage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;

import java.util.List;
import java.util.stream.Stream;

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

        HttpEntity<CreateMenuItemDto> httpEntity = new HttpEntity<>(new CreateMenuItemDto("Sarma", "Jelo od mlevenog mesa i kiselog kupusa", MenuItemType.FOOD,
                "Glavno jelo", 10, null), headers);
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
    public void createMenuItem_InvalidMenuItemName_ReturnsBadRequest() {
        int size = menuItemService.findAll().size();

        HttpEntity<CreateMenuItemDto> httpEntity = new HttpEntity<>(new CreateMenuItemDto("", "Jelo od mlevenog mesa i kiselog kupusa", MenuItemType.FOOD,
                "Glavno jelo", 10, null), headers);
        ResponseEntity<MenuItem> responseEntity = restTemplate.postForEntity("/api/v1/menu-items", httpEntity, MenuItem.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(size, menuItemService.findAll().size());
    }

    @Test
    public void getMenuItemById_ValidMenuItemId_ReturnsOk() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<MenuItemDto> responseEntity = restTemplate.exchange("/api/v1/menu-items/{id}", HttpMethod.GET, httpEntity, MenuItemDto.class, 1);

        MenuItemDto menuItem = responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("coca cola", menuItem.getName());
        assertEquals("bezalkoholno gazirano pice", menuItem.getDescription());
        assertEquals(MenuItemType.DRINK, menuItem.getType());
        assertEquals(MenuItemCategory.NON_ALCOHOLIC, MenuItemCategory.findCategory(menuItem.getCategory()));
        assertEquals(2, menuItem.getPreparationTime());
    }

    @Test
    public void getMenuItemById_InvalidMenuItemId_ReturnsNotFound() {
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
                "Bezalkoholno piće", 1, true, 2), headers);
        ResponseEntity<MenuItemDto> responseEntity = restTemplate.exchange("/api/v1/menu-items/{id}", HttpMethod.PUT, httpEntity, MenuItemDto.class, 1);

        MenuItemDto menuItem = responseEntity.getBody();

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
                "Bezalkoholno piće", 1, true, 2), headers);
        ResponseEntity<MenuItem> responseEntity = restTemplate.exchange("/api/v1/menu-items/{id}", HttpMethod.PUT, httpEntity, MenuItem.class, 20);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void updateMenuItem_InvalidMenuItemName_ReturnsBadRequest() {
        HttpEntity<UpdateMenuItemDto> httpEntity = new HttpEntity<>(new UpdateMenuItemDto("", "bezalkoholno gazirano pice", MenuItemType.DRINK,
                "Bezalkoholno piće", 1, true, 2), headers);
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
        ResponseEntity<MenuItemDto[]> responseEntity = restTemplate.exchange("/api/v1/menu-items/search/{search}", HttpMethod.GET, httpEntity,
                MenuItemDto[].class, "bezalkoholno");

        List<MenuItemDto> menuItems = List.of(responseEntity.getBody());

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
        ResponseEntity<MenuItemDto[]> responseEntity = restTemplate.exchange("/api/v1/menu-items/filter/{filter}", HttpMethod.GET, httpEntity,
                MenuItemDto[].class, "Dezert");

        List<MenuItemDto> menuItems = List.of(responseEntity.getBody());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(3, menuItems.size());
        assertEquals(MenuItemCategory.DESSERT, MenuItemCategory.findCategory(menuItems.get(0).getCategory()));
        assertEquals(MenuItemCategory.DESSERT, MenuItemCategory.findCategory(menuItems.get(1).getCategory()));
        assertEquals(MenuItemCategory.DESSERT, MenuItemCategory.findCategory(menuItems.get(2).getCategory()));
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

    @Test
    public void findMenuItemsByMenuId_ValidMenuId_Pageable_ReturnsOk() {
        ParameterizedTypeReference<RestResponsePage<MenuItemDto>> responseType = new ParameterizedTypeReference<RestResponsePage<MenuItemDto>>() { };

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<RestResponsePage<MenuItemDto>> responseEntity = restTemplate.exchange("/api/v1/menu-items/by-menu/{menuId}?page={page}&size={size}",
                HttpMethod.GET, httpEntity, responseType, 1, 1, 5);

        List<MenuItemDto> menuItems = List.of(responseEntity.getBody().getContent().toArray(new MenuItemDto[0]));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(5, menuItems.size());
    }

    @Test
    public void findMenuItemsByMenuId_InvalidMenuIdPageable_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<MissingEntityException> responseEntity = restTemplate.exchange("/api/v1/menu-items/by-menu/{menuId}?page={page}&size={size}",
                HttpMethod.GET, httpEntity, MissingEntityException.class, 10, 1, 5);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void searchAndFilterMenuItems_MenuIdSearchParamFilterPageable_ReturnsNotFound() {
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<MissingEntityException> responseEntity = restTemplate.exchange("/api/v1/menu-items/by-menu/{menuId}/search?page={page}&size={size}" +
                        "&searchParam={searchParam}&filter={filter}",
                HttpMethod.GET, httpEntity, MissingEntityException.class, 10, 1, 5, "", "");

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @ParameterizedTest
    @MethodSource("getMenuIdSearchParamFilterAndPageable")
    public void searchAndFilterMenuItems_MenuIdSearchParamFilterPageable_ReturnsOk(Integer menuId, String searchParam, String filter,
                                                                                   Integer page, Integer pageSize, Integer expectedSize) {
        ParameterizedTypeReference<RestResponsePage<MenuItemDto>> responseType = new ParameterizedTypeReference<RestResponsePage<MenuItemDto>>() { };

        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<RestResponsePage<MenuItemDto>> responseEntity = restTemplate.exchange("/api/v1/menu-items/by-menu/{menuId}/search?page={page}&size={size}" +
                        "&searchParam={searchParam}&filter={filter}",
                HttpMethod.GET, httpEntity, responseType, menuId, page, pageSize, searchParam, filter);

        List<MenuItemDto> menuItems = List.of(responseEntity.getBody().getContent().toArray(new MenuItemDto[0]));

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedSize, menuItems.size());
    }

    private static Stream<Arguments> getMenuIdSearchParamFilterAndPageable() {
        return Stream.of(
                Arguments.of(1, "", "", 0, 5, 5),
                Arguments.of(1, "cola", "", 0, 5, 1),
                Arguments.of(1, "cola", "Bezalkoholno piće",0, 5, 1),
                Arguments.of(1, "", "Bezalkoholno piće", 0, 5, 2)
        );
    }
}
