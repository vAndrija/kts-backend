package com.kti.restaurant.e2e.tests;

import com.kti.restaurant.e2e.pages.*;
import com.kti.restaurant.e2e.utils.WaitUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateOrderE2ETest {
    private WebDriver driver;

    private LoginPage loginPage;

    private MenuItemsListPage menuItemsListPage;

    private OrderItemsPage orderItemsPage;

    private OrderPage orderPage;

    private RestaurantPreviewPage restaurantPreviewPage;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        restaurantPreviewPage = PageFactory.initElements(driver, RestaurantPreviewPage.class);
        orderItemsPage = PageFactory.initElements(driver, OrderItemsPage.class);
        orderPage = PageFactory.initElements(driver, OrderPage.class);
        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
    }

    @Test
    public void filterAndSearchMenuItemsWhenCreateOrder() {
        loginPage.login("jovanpetrovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));

        menuItemsListPage.clickRestaurantButton();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/restaurant/preview", 10));

        new Actions(driver).moveToElement(restaurantPreviewPage.getCanvas()).moveByOffset(304, 11).click().perform();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/order/order", 10));
        orderItemsPage.setInputSearch("supa");

        orderItemsPage.clickButtonSearch();

        assertEquals(2, orderItemsPage.getMenuItemsByCategory(2).size());

        orderItemsPage.clickCategoryButton();

        assertEquals(3, orderItemsPage.getMenuItemsByCategory(3).size());

        orderItemsPage.clickLoadMoreButton();

        assertEquals(4, orderItemsPage.getMenuItemsByCategory(4).size());


        orderItemsPage.clickAllCategoryButton();


    }

    @Test
    public void createOrder() {
        loginPage.login("jovanpetrovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));

        menuItemsListPage.clickRestaurantButton();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/restaurant/preview", 10));

        new Actions(driver).moveToElement(restaurantPreviewPage.getCanvas()).moveByOffset(304, -102).click().perform();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/order/order", 10));

        orderItemsPage.setQuantity("2");
        orderItemsPage.setPriority("2");
        orderItemsPage.setNote("sa ledom");
        orderItemsPage.clickAddButton();

        orderItemsPage.setQuantitySecond("3");
        orderItemsPage.setPrioritySecond("1");
        orderItemsPage.clickAddButtonSecond();

        orderItemsPage.clickOrderButton();

        assertEquals("coca cola", orderPage.getMenuItemName().getText());
        assertEquals("2", orderPage.getMenuItemQuantity().getText());
        assertEquals("360RSD", orderPage.getMenuItemPrice().getText());
        assertEquals("sa ledom", orderPage.getMenuItemNote().getText());

        orderPage.clickDeleteButton();


        assertEquals(2, orderPage.getRows().size());


        orderPage.clickCreateOrderButton();
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/order/review", 10));

    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }

}
