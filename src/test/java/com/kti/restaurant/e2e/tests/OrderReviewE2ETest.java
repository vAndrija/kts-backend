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

public class OrderReviewE2ETest {
    private WebDriver driver;

    private LoginPage loginPage;

    private MenuItemsListPage menuItemsListPage;

    private OrderReviewPage orderReviewPage;

    private RestaurantPreviewPage restaurantPreviewPage;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        restaurantPreviewPage = PageFactory.initElements(driver, RestaurantPreviewPage.class);
        orderReviewPage = PageFactory.initElements(driver, OrderReviewPage.class);
        menuItemsListPage = PageFactory.initElements(driver, MenuItemsListPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);

        driver.manage().window().maximize();
        driver.get("http://localhost:4200/auth/login");
    }

    @Test
    public void viewOrder() {
        loginPage.login("jovanpetrovic@gmail.com", "123");

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/menu/menu-items", 10));

        menuItemsListPage.clickRestaurantButton();

        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/restaurant/preview", 10));


        new Actions(driver).moveToElement(restaurantPreviewPage.getCanvas()).moveByOffset(470, 9).click().perform();
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/order/review", 10));

        assertEquals(10, orderReviewPage.getRows(10).size());

        orderReviewPage.clickDeleteButton();

        assertEquals(9, orderReviewPage.getRows(9).size());

        orderReviewPage.clickCreateOrderButton();
        assertTrue(WaitUtils.urlWait(driver, "http://localhost:4200/order/order", 10));

    }

    @AfterEach
    public void tearDown() {
        driver.quit();
    }


}
